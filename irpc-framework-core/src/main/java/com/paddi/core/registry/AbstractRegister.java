package com.paddi.core.registry;

import java.util.List;
import java.util.Map;

import static com.paddi.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static com.paddi.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:40:56
 */
public abstract class AbstractRegister implements RegistryService{
    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    /**
     * 获取服务的权重信息
     *
     * @param serviceName
     * @return <ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>
     */
    public abstract Map<String, String> getServiceWeightMap(String serviceName);
    /**
     * 子类进行拓展
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 子类进行拓展
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 子类进行拓展
     * @param serviceName
     */
    public abstract List<String> getProviderIps(String serviceName);

}
