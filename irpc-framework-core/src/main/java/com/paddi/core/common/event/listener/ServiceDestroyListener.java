package com.paddi.core.common.event.listener;

import com.paddi.core.common.event.RpcDestroyEvent;
import com.paddi.core.registry.URL;

import static com.paddi.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.paddi.core.common.cache.CommonServerCache.REGISTRY_SERVICE;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 15:27:07
 */
public class ServiceDestroyListener implements RpcListener<RpcDestroyEvent> {
    @Override
    public void callBack(Object t) {
        for(URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}
