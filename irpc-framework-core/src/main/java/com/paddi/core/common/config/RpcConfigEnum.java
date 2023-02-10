package com.paddi.core.common.config;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 12:15:31
 */
public enum RpcConfigEnum {
    RPC_CONFIG_PATH("rpc.properties"),
    REGISTRY_ADDRESS("rpc.registry.address"),
    REGISTRY_TYPE("rpc.registry.type"),
    SERVER_PORT("rpc.server.port"),
    APPLICATION_NAME("rpc.application.name"),
    PROXY_TYPE("rpc.proxy.type"),
    ROUTER_STRATEGY("rpc.router.strategy"),
    SERIALIZE_TYPE("rpc.serialize.type"),
    COMPRESS_TYPE("rpc.compress.type"),
    THREAD_CORE_POOL_SIZE("rpc.thread.corePoolSize"),
    THREAD_MAXIMUM_POOL_SIZE("rpc.thread.maximumPoolSize"),
    THREAD_KEEPALIVE_TIME("rpc.thread.keepAliveTime"),
    THREAD_KEEPALIVE_TIMEUNIT("rpc.thread.keepAliveTimeunit"),
    THREAD_QUEUE_MAX_SIZE("rpc.thread.queueMaxSize"),
    SERVER_MAXIMUM_CONNECTION("rpc.server.maxConnection"),
    INVOKE_TIMEOUT("rpc.invoke.timeout")
    ;

    private final String propertyValue;
    RpcConfigEnum(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
