package com.paddi.core.client;

import com.paddi.core.proxy.ProxyFactory;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:19:32
 */
public class RpcReference {
    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param rpcReferenceWrapper
     * @param <T>
     * @return
     */
    public <T> T get(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        return proxyFactory.getProxy(rpcReferenceWrapper);
    }
}
