package com.paddi.core.common.event.listener;

import com.paddi.core.client.ConnectionHandler;
import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.event.RpcUpdateEvent;
import com.paddi.core.common.event.data.URLChangeWrapper;
import com.paddi.core.registry.URL;
import com.paddi.core.registry.zookeeper.ProviderNodeInfo;
import com.paddi.core.router.Selector;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.paddi.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.paddi.core.common.cache.CommonClientCache.ROUTER;


/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:22:27
 */
public class ServiceUpdateListener implements RpcListener<RpcUpdateEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ServiceUpdateListener.class);

    @Override
    public void callBack(Object t) {
        //TODO 监听到新的节点上线 应该去订阅这些节点的变化
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        logger.info("service update, name: {}, url: {} ", urlChangeWrapper.getServiceName(), urlChangeWrapper.getProviderUrl());
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(urlChangeWrapper.getServiceName());

        List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
        HashSet<String> finalUrl = new HashSet<>();
        List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
        if(channelFutureWrappers != null) {
            for(ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
                String oldServerAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
                if(matchProviderUrl.contains(oldServerAddress)) {
                    finalChannelFutureWrappers.add(channelFutureWrapper);
                    finalUrl.add(oldServerAddress);
                }
            }
        }
        //此时老的url已经被移除了，开始检查是否有新的url
        //ChannelFutureWrapper是一个自定义的包装类，将netty建立好的ChannelFuture做了一些封装
        List<ChannelFutureWrapper> newChannelFutureWrapper = new ArrayList<>();
        for(String newProviderUrl : matchProviderUrl) {
            if(!finalUrl.contains(newProviderUrl)) {
                ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                String host = newProviderUrl.split(":")[0];
                Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                channelFutureWrapper.setHost(host);
                channelFutureWrapper.setPort(port);
                String urlString = urlChangeWrapper.getNodeDataUrl().get(newProviderUrl);
                ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(urlString);
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                ChannelFuture channelFuture = null;
                try {
                    channelFuture = ConnectionHandler.createChannelFuture(host,port);
                    channelFutureWrapper.setChannelFuture(channelFuture);
                    newChannelFutureWrapper.add(channelFutureWrapper);
                    finalUrl.add(newProviderUrl);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finalChannelFutureWrappers.addAll(newChannelFutureWrapper);
        //最终更新服务信息
        CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureWrappers);
        Selector selector = new Selector();
        selector.setProviderServiceName(urlChangeWrapper.getServiceName());
        ROUTER.refreshRouter(selector);
    }
}
