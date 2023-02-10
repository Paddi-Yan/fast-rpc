package com.paddi.core.common.constants;

import com.paddi.core.common.config.ServerConfig;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:11:56
 */
public class RpcConstants {
    /*************************protocol*************************/
    public static final short MAGIC_NUMBER=1989;
    public static final int MAGIC_NUMBERS = 1989;
    public static final short VERSION = 1;
    public static final int HEADER_LENGTH = 32;
    public static final int EXTENSION_OPTIONS_TIMESTAMP = 13;
    public static final short REQUEST_TYPE = 1;
    public static final short RESPONSE_TYPE = 2;
    public static final short HEARTBEAT_REQUEST_TYPE = 3;
    public static final short HEARTBEAT_RESPONSE_TYPE = 4;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
    /*************************protocol*************************/

    public static final String PORT = "port";
    public static final String HOST = "host";

    public static final String GROUP = "group";

    public static final String LIMIT = "limit";

    public static final String WEIGHT = "weight";

    public static final String URL_PARAMETER_SERVICE_PATH = "servicePath";

    public static final String URL_PARAMETER_PROVIDER_IPS = "providerIps";

    /*************************invoke-config*************************/
    public static final String TIMEOUT = "timeout";
    public static final Integer DEFAULT_TIMEOUT = 3 * 1000;


    /*************************router-type*************************/

    public static final String ZOOKEEPER_REGISTRY_TYPE = "zookeeper";


    /*************************router-type*************************/

    public static final String RANDOM_ROUTER_TYPE = "random";

    public static final String ROTATE_ROUTER_TYPE = "rotate";

    /*************************proxy-type*************************/

    public static final String JAVASSIST_PROXY = "javassist";
    public static final String JDK_PROXY = "jdk";

    /*************************serialize-type*************************/

    public static final String JDK_SERIALIZE_TYPE = "jdk";

    public static final String FASTJSON_SERIALIZE_TYPE = "fastjson";

    public static final String HESSIAN2_SERIALIZE_TYPE = "hessian2";
    public static final String KRYO_SERIALIZE_TYPE = "kryo";


    public static final Integer DEFAULT_MAX_CONNECTION_NUMS = ServerConfig.THREAD_MAXIMUM_POOL_SIZE + ServerConfig.THREAD_QUEUE_MAX_SIZE;

    public static final String DEFAULT_DECODE_CHAR = "$_i0#Xsop1_$";

    public static final int SERVER_DEFAULT_MSG_LENGTH = 1024 * 10;

    public static final int CLIENT_DEFAULT_MSG_LENGTH = 1024 * 10;

    public static final String FILTER_BEFORE = "before";

    public static final String FILTER_AFTER = "after";
}
