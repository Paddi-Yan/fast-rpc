package com.paddi.core.client;

import com.alibaba.fastjson.JSON;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.RpcMessageDecoder;
import com.paddi.core.common.RpcMessageEncoder;
import com.paddi.core.common.RpcProtocol;
import com.paddi.core.common.config.ClientConfig;
import com.paddi.core.common.config.PropertiesBootstrap;
import com.paddi.core.common.event.RpcListenerLoader;
import com.paddi.core.extension.ExtensionLoader;
import com.paddi.core.filter.ClientFilter;
import com.paddi.core.filter.client.ClientFilterChain;
import com.paddi.core.proxy.ProxyFactory;
import com.paddi.core.registry.AbstractRegister;
import com.paddi.core.registry.RegistryService;
import com.paddi.core.registry.URL;
import com.paddi.core.router.Router;
import com.paddi.core.serialize.SerializeFactory;
import com.paddi.core.utils.CommonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.paddi.core.common.cache.CommonClientCache.*;
import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:33:15
 */
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private ClientConfig clientConfig;
    private RpcListenerLoader rpcListenerLoader;
    private Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference startClientApplication() throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //管道中初始化一些逻辑，这里包含了上边所说的编解码器和客户端响应类
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 10, delimiter));
                ch.pipeline().addLast(new RpcMessageEncoder());
                ch.pipeline().addLast(new RpcMessageDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        CLIENT_CONFIG = clientConfig;
        //SPI拓展加载
        this.initClientConfig();
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class)
                                                .getExtension(clientConfig.getProxyType());
        return new RpcReference(proxyFactory);
    }

    private void initClientConfig() {
        String routerStrategy = clientConfig.getRouterStrategy();
        ROUTER = ExtensionLoader.getExtensionLoader(Router.class).getExtension(routerStrategy);
        String serializeType = clientConfig.getSerializeType();
        CLIENT_SERIALIZE_FACTORY = ExtensionLoader.getExtensionLoader(SerializeFactory.class)
                                                    .getExtension(serializeType);
        ClientFilterChain clientFilterChain = new ClientFilterChain();
        List<ClientFilter> clientFilters = ExtensionLoader.getExtensionLoader(ClientFilter.class).getAllExtension();
        for(ClientFilter clientFilter : clientFilters) {
            clientFilterChain.addClientFilter(clientFilter);
        }
        CLIENT_FILTER_CHAIN = clientFilterChain;
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if(ABSTRACT_REGISTER == null) {
            ABSTRACT_REGISTER = (AbstractRegister) ExtensionLoader.getExtensionLoader(RegistryService.class)
                                                                  .getExtension(clientConfig.getRegisterType());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter(HOST, CommonUtils.getIpAddress());
        //TODO 更新
        Map<String, String> result = ABSTRACT_REGISTER.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(), result);
        ABSTRACT_REGISTER.subscribe(url);
    }

    public void doConnectServer() {
        for(URL providerUrl : SUBSCRIBE_SERVICE_LIST) {
            String providerServiceName = providerUrl.getServiceName();
            List<String> providerIps = ABSTRACT_REGISTER.getProviderIps(providerServiceName);
            for(String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerIp);
                } catch(Exception e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(providerServiceName);
            url.addParameter(URL_PARAMETER_SERVICE_PATH, providerUrl.getServiceName() + "/provider");
            url.addParameter(URL_PARAMETER_PROVIDER_IPS, JSON.toJSONString(providerIps));
            ABSTRACT_REGISTER.doAfterSubscribe(url);
        }
    }




    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    class AsyncSendJob implements Runnable {

        @Override
        public void run() {
            while(true) {
                try {
                    RpcInvocation rpcInvocation = SEND_QUEUE.take();
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(rpcInvocation);
                    if(channelFuture != null) {
                        Channel channel = channelFuture.channel();
                        if(!channel.isOpen()) {
                            throw new RuntimeException("target service channel is not open!");
                        }
                        //将RpcInvocation封装到RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
                        RpcProtocol rpcProtocol = new RpcProtocol(CLIENT_SERIALIZE_FACTORY.serialize(rpcInvocation));
                        logger.info("RpcInvocation RequestId: {}", rpcInvocation.getUuid());
                        channel.writeAndFlush(rpcProtocol);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
