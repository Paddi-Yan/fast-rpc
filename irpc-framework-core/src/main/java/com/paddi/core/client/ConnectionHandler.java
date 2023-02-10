package com.paddi.core.client;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.registry.URL;
import com.paddi.core.registry.zookeeper.ProviderNodeInfo;
import com.paddi.core.router.Selector;
import com.paddi.core.utils.CommonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.paddi.core.common.cache.CommonClientCache.*;

/**
 * 职责：当注册中心的节点新增或者移除或者权重变化的时候，这个类主要负责对内存中的url做变更
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:49:38
 */
@Slf4j
public class ConnectionHandler {
    /**
     * 核心的连接处理器
     * 专门用于负责和服务端构建连接通信
     */
    private static Bootstrap bootstrap;

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    /**
     * 构建单个连接通道 元操作，既要处理连接，还要统一将连接进行内存存储管理
     * @param providerServiceName
     * @param providerIp
     * @throws Exception
     */
    public static void connect(String providerServiceName, String providerIp) throws Exception {
        if(bootstrap == null) {
            throw new RuntimeException("boostrap can not be null!");
        }
        if(!providerIp.contains(":")) {
            return;
        }
        String[] providerAddress = providerIp.split(":");
        String ip = providerAddress[0];
        Integer port = Integer.valueOf(providerAddress[1]);
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        String providerUrlInfo = URL_MAP.get(providerServiceName).get(providerIp);
        ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(providerUrlInfo);
        ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
        channelFutureWrapper.setChannelFuture(channelFuture);
        channelFutureWrapper.setHost(ip);
        channelFutureWrapper.setPort(port);
        channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
        channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
        SERVER_ADDRESS.add(providerIp);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerServiceName);
        if(CommonUtils.isEmptyList(channelFutureWrappers)) {
            channelFutureWrappers = new ArrayList<>();
        }
        channelFutureWrappers.add(channelFutureWrapper);
        CONNECT_MAP.put(providerServiceName, channelFutureWrappers);
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        ROUTER.refreshRouter(selector);
    }


    public static ChannelFuture createChannelFuture(String host, Integer port) throws InterruptedException {
        return bootstrap.connect(host, port).sync();
    }

    public static void disconnect(String providerServiceName, String providerIp) {
        SERVER_ADDRESS.remove(providerIp);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerServiceName);
        if(CommonUtils.isNotEmptyList(channelFutureWrappers)) {
            Iterator<ChannelFutureWrapper> iterator = channelFutureWrappers.iterator();
            while(iterator.hasNext()) {
                ChannelFutureWrapper channelFutureWrapper = iterator.next();
                if(providerIp.equals(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort())) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 默认走随机策略获取ChannelFuture
     *
     * @param rpcInvocation
     * @return
     */
    public static ChannelFuture getChannelFuture(RpcInvocation rpcInvocation) {
        String providerServiceName = rpcInvocation.getTargetServiceName();
        ChannelFutureWrapper[] channelFutureWrappers = SERVICE_ROUTER_MAP.get(providerServiceName);
        if(channelFutureWrappers == null || channelFutureWrappers.length == 0) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setException(new RuntimeException("no provider exist for " + providerServiceName));
            rpcInvocation.setResponse(null);
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            log.error("no provider exist for {}", providerServiceName);
            return null;
        }
        //执行过滤链
        List<ChannelFutureWrapper> channelFutureWrapperList = Arrays.stream(channelFutureWrappers).collect(Collectors.toList());
        CLIENT_FILTER_CHAIN.doFilter(channelFutureWrapperList, rpcInvocation);
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        selector.setChannelFutureWrappers(channelFutureWrappers);
        ChannelFutureWrapper channelFutureWrapper = ROUTER.select(selector);
        return channelFutureWrapper.getChannelFuture();
    }
}
