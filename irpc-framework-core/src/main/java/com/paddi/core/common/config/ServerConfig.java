package com.paddi.core.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:08:34
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ServerConfig {
    /************************DEFAULT CONFIG****************************/
    public static final Integer SERVER_PORT = 8888;
    public static final String REGISTRY_ADDR = "127.0.0.1";
    public static final String APPLICATION_NAME = "rpc-service-provider";
    public static final String SERIALIZE_TYPE = KRYO_SERIALIZE_TYPE;
    public static final String REGISTER_TYPE = ZOOKEEPER_REGISTRY_TYPE;
    public static final Integer THREAD_CORE_POOL_SIZE = 5;
    public static final Integer THREAD_MAXIMUM_POOL_SIZE = 10;
    public static final Integer THREAD_KEEPALIVE_TIME = 10;
    public static final TimeUnit THREAD_KEEPALIVE_TIMEUNIT = TimeUnit.SECONDS;
    public static final Integer THREAD_QUEUE_MAX_SIZE = 512;

    public static final Integer SERVER_MAXIMUM_CONNECTIONS = DEFAULT_MAX_CONNECTION_NUMS;

    public static final String DEFAULT_COMPRESS_TYPE = "gzip";
    /************************DEFAULT CONFIG****************************/

    private Integer serverPort;

    private String registerAddr;

    private String applicationName;

    private String serializeType;

    private String compressType;

    private String registerType;

    private Integer threadCorePoolSize;

    private Integer threadMaximumPoolSize;

    private Integer threadKeepAliveTime;

    private TimeUnit threadKeepAliveTimeUnit;

    private Integer threadQueueMaxSize;

    private Integer serverMaximumConnections;
}
