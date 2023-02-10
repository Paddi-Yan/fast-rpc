package com.paddi.core.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:35:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientConfig {
    /************************DEFAULT CONFIG****************************/
    public static final String REGISTRY_ADDR = "127.0.0.1";
    public static final String APPLICATION_NAME = "rpc-service-consumer";
    public static final String SERIALIZE_TYPE = KRYO_SERIALIZE_TYPE;
    public static final String REGISTER_TYPE = ZOOKEEPER_REGISTRY_TYPE;
    public static final String ROUTER_STRATEGY = RANDOM_ROUTER_TYPE;
    public static final String PROXY_TYPE = JDK_PROXY;
    public static final Integer TIMEOUT = DEFAULT_TIMEOUT;

    public static final String DEFAULT_COMPRESS_TYPE = "gzip";
    /************************DEFAULT CONFIG****************************/

    private String applicationName;

    private String registerAddr;

    private String proxyType;


    /**
     * 负载均衡策略 example:random,rotate
     */
    private String routerStrategy;

    private String serializeType;

    private String compressType;

    private String registerType;

    private Integer timeout;
}
