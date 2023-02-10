package com.paddi.core.common.cache;

import com.paddi.core.common.ChannelFuturePollingRef;
import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.config.ClientConfig;
import com.paddi.core.filter.client.ClientFilterChain;
import com.paddi.core.registry.AbstractRegister;
import com.paddi.core.registry.URL;
import com.paddi.core.router.Router;
import com.paddi.core.serialize.SerializeFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:31:47
 */
public class CommonClientCache {
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();
    public static ClientConfig CLIENT_CONFIG;
    /**
     * providerNameList
     */
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    /**
     * com.paddi.service -> <<ip:host,urlString>,<ip:host,urlString>,<ip:host,urlString>>
     */
    public static Map<String, Map<String, String>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    /**
     * 每次进行远程调用的时候都是从这里面去选择服务提供者
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    /**
     * 随机请求调用Map
     */
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    public static Router ROUTER;
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;
    public static ClientFilterChain CLIENT_FILTER_CHAIN;

    public static AbstractRegister ABSTRACT_REGISTER;
}
