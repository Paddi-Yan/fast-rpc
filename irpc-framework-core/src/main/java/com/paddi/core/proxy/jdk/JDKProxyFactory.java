package com.paddi.core.proxy.jdk;

import com.paddi.core.client.RpcReferenceWrapper;
import com.paddi.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:45:05
 */
public class JDKProxyFactory implements ProxyFactory {
    public JDKProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        Class<T> clazz = rpcReferenceWrapper.getTargetClass();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JDKClientInvocationHandler(rpcReferenceWrapper));
    }
}
