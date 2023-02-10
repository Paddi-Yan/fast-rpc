package com.paddi.core.common.cache;

import com.paddi.core.common.ServiceSemaphoreWrapper;
import com.paddi.core.common.config.ServerConfig;
import com.paddi.core.dispatcher.RequestDispatcher;
import com.paddi.core.filter.server.ServiceAfterFilterChain;
import com.paddi.core.filter.server.ServiceBeforeFilterChain;
import com.paddi.core.registry.RegistryService;
import com.paddi.core.registry.URL;
import com.paddi.core.serialize.SerializeFactory;
import com.paddi.core.server.ServiceWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:31:28
 */
public class CommonServerCache {
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new HashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
    public static  Boolean IS_STARTED = false;
    public static RegistryService REGISTRY_SERVICE;
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;

    public static ServerConfig SERVER_CONFIG;

    public static ServiceBeforeFilterChain SERVICE_BEFORE_FILTER_CHAIN;
    public static ServiceAfterFilterChain SERVICE_AFTER_FILTER_CHAIN;

    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();

    /**
     * 请求任务分发器和处理器
     */
    public static RequestDispatcher REQUEST_DISPATCHER = new RequestDispatcher();

    public static Map<String, ServiceSemaphoreWrapper> SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>();
}
