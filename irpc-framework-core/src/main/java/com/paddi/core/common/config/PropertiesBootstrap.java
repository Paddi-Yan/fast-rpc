package com.paddi.core.common.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 将properties的配置转换成本地的一个Map结构进行管理
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 12:12:14
 */
public class PropertiesBootstrap {
    private volatile boolean configIsReady;

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch(IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.SERVER_PORT.getPropertyValue(), ServerConfig.SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.APPLICATION_NAME.getPropertyValue(), ServerConfig.APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.REGISTRY_ADDRESS.getPropertyValue(), ServerConfig.REGISTRY_ADDR));
        serverConfig.setSerializeType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.SERIALIZE_TYPE.getPropertyValue(), ServerConfig.SERIALIZE_TYPE));
        serverConfig.setCompressType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.COMPRESS_TYPE.getPropertyValue(), ServerConfig.DEFAULT_COMPRESS_TYPE));
        serverConfig.setRegisterType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.REGISTRY_TYPE.getPropertyValue(), ServerConfig.REGISTER_TYPE));
        serverConfig.setThreadCorePoolSize(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.THREAD_CORE_POOL_SIZE.getPropertyValue(), ServerConfig.THREAD_CORE_POOL_SIZE));
        serverConfig.setThreadMaximumPoolSize(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.THREAD_MAXIMUM_POOL_SIZE.getPropertyValue(), ServerConfig.THREAD_MAXIMUM_POOL_SIZE));
        String keepAliveTimeUnit = PropertiesLoader.getPropertiesString(RpcConfigEnum.THREAD_KEEPALIVE_TIMEUNIT.getPropertyValue());
        serverConfig.setThreadKeepAliveTime(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.THREAD_KEEPALIVE_TIME.getPropertyValue(), ServerConfig.THREAD_KEEPALIVE_TIME));
        serverConfig.setThreadKeepAliveTimeUnit(keepAliveTimeUnit == null ? ServerConfig.THREAD_KEEPALIVE_TIMEUNIT : TimeUnit.valueOf(keepAliveTimeUnit.toUpperCase()));
        serverConfig.setThreadQueueMaxSize(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.THREAD_QUEUE_MAX_SIZE.getPropertyValue(), ServerConfig.THREAD_QUEUE_MAX_SIZE));
        serverConfig.setServerMaximumConnections(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.SERVER_MAXIMUM_CONNECTION.getPropertyValue(), ServerConfig.SERVER_MAXIMUM_CONNECTIONS));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch(IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.APPLICATION_NAME.getPropertyValue(), ClientConfig.APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.REGISTRY_ADDRESS.getPropertyValue(), ClientConfig.REGISTRY_ADDR));
        clientConfig.setSerializeType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.SERIALIZE_TYPE.getPropertyValue(), ClientConfig.SERIALIZE_TYPE));
        clientConfig.setCompressType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.COMPRESS_TYPE.getPropertyValue(), ClientConfig.DEFAULT_COMPRESS_TYPE));
        clientConfig.setRegisterType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.REGISTRY_TYPE.getPropertyValue(), ClientConfig.REGISTER_TYPE));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.PROXY_TYPE.getPropertyValue(), ClientConfig.PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStringOrDefault(RpcConfigEnum.ROUTER_STRATEGY.getPropertyValue(), ClientConfig.ROUTER_STRATEGY));
        clientConfig.setTimeout(PropertiesLoader.getPropertiesIntegerOrDefault(RpcConfigEnum.INVOKE_TIMEOUT.getPropertyValue(), ClientConfig.TIMEOUT));
        return clientConfig;
    }

    public static void main(String[] args) {
        ServerConfig serverConfig = loadServerConfigFromLocal();
        System.out.println("serverConfig = " + serverConfig);
    }
}
