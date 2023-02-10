package com.paddi.core.server;

import com.paddi.core.common.RpcMessageDecoder;
import com.paddi.core.common.RpcMessageEncoder;
import com.paddi.core.common.ServiceSemaphoreWrapper;
import com.paddi.core.common.config.PropertiesBootstrap;
import com.paddi.core.common.config.ServerConfig;
import com.paddi.core.extension.ExtensionLoader;
import com.paddi.core.filter.FilterOrder;
import com.paddi.core.filter.ServerFilter;
import com.paddi.core.filter.server.ServiceAfterFilterChain;
import com.paddi.core.filter.server.ServiceBeforeFilterChain;
import com.paddi.core.registry.RegistryService;
import com.paddi.core.registry.URL;
import com.paddi.core.serialize.SerializeFactory;
import com.paddi.core.utils.CommonUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Semaphore;

import static com.paddi.core.common.cache.CommonServerCache.*;
import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月06日 23:49:54
 */
@Slf4j
public class Server {
    private static EventLoopGroup bossGroup = null;

    private static EventLoopGroup workerGroup = null;

    private static MaxConnectionLimitHandler maxConnectionLimitHandler = null;

    private ServerConfig serverConfig;


    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        maxConnectionLimitHandler = new MaxConnectionLimitHandler(serverConfig.getServerMaximumConnections());
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                 .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                 .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(maxConnectionLimitHandler);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 10, delimiter));
                ch.pipeline().addLast(new RpcMessageEncoder());
                ch.pipeline().addLast(new RpcMessageDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        //准备接收并消费请求消息
        REQUEST_DISPATCHER.startRequestConsumer();
        bootstrap.bind(serverConfig.getServerPort()).sync();
        IS_STARTED = true;
        log.info("rpc server application is started");
    }

    public void initServerConfig() {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
        SERVER_CONFIG = serverConfig;
        //初始化线程池和队列配置
        REQUEST_DISPATCHER.init(serverConfig.getThreadCorePoolSize(),
                serverConfig.getThreadMaximumPoolSize(),
                serverConfig.getThreadKeepAliveTime(),
                serverConfig.getThreadKeepAliveTimeUnit(),
                serverConfig.getThreadQueueMaxSize());
        //注册中心
        REGISTRY_SERVICE = ExtensionLoader.getExtensionLoader(RegistryService.class)
                                          .getExtension(serverConfig.getRegisterType());
        //初始化序列化配置
        String serializeType = serverConfig.getSerializeType();
        SERVER_SERIALIZE_FACTORY = ExtensionLoader.getExtensionLoader(SerializeFactory.class)
                                                    .getExtension(serializeType);
        //初始化过滤链
        List<ServerFilter> filters = ExtensionLoader.getExtensionLoader(ServerFilter.class).getAllExtension();
        ServiceBeforeFilterChain serviceBeforeFilterChain = new ServiceBeforeFilterChain();
        ServiceAfterFilterChain serviceAfterFilterChain = new ServiceAfterFilterChain();
        for(ServerFilter filter : filters) {
            Class<? extends ServerFilter> filterClass = filter.getClass();
            FilterOrder filterOrder = filterClass.getDeclaredAnnotation(FilterOrder.class);
            if(filterOrder != null && FILTER_BEFORE.equals(filterOrder.order())) {
                serviceBeforeFilterChain.addServerFilter(filter);
            }else if(filterClass != null && FILTER_AFTER.equals(filterOrder.order())) {
                serviceAfterFilterChain.addServerFilter(filter);
            }
        }
        SERVICE_BEFORE_FILTER_CHAIN = serviceBeforeFilterChain;
        SERVICE_AFTER_FILTER_CHAIN = serviceAfterFilterChain;
    }

    private void batchExportUrl() {
        Thread task = new Thread(() -> {
            try {
                Thread.sleep(2500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            for(URL url : PROVIDER_URL_SET) {
                REGISTRY_SERVICE.register(url);
                log.info("{} export service: {}", url.getApplicationName(), url.getServiceName());
            }
        });
        task.start();
    }

    public void exportService(ServiceWrapper serviceWrapper) {
        Object serviceBean = serviceWrapper.getService();
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if(classes.length == 0) {
            throw new RuntimeException("service must has interfaces!");
        }
        if(classes.length > 1) {
            throw new RuntimeException("service must only has one interfaces!");
        }
        //默认选择该对象的第一个实现接口
        Class<?> interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter(HOST, CommonUtils.getIpAddress());
        url.addParameter(PORT, String.valueOf(serverConfig.getServerPort()));
        url.addParameter(GROUP, serviceWrapper.getGroup());
        url.addParameter(LIMIT, String.valueOf(serviceWrapper.getLimit()));
        url.addParameter(WEIGHT, String.valueOf(serviceWrapper.getWeight()));
        //设置接口的限流器
        SERVICE_SEMAPHORE_MAP.put(interfaceClass.getName(), new ServiceSemaphoreWrapper(serviceWrapper.getLimit()));
        PROVIDER_URL_SET.add(url);
        //设置接口请求TOKEN
        if(CommonUtils.isEmpty(serviceWrapper.getToken())) {
            PROVIDER_SERVICE_WRAPPER_MAP.put(interfaceClass.getName(), serviceWrapper);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        for(int i = 0; i < 10; i++) {
            boolean result = semaphore.tryAcquire();
            System.out.println(result);
        }
    }



}
