package com.paddi.core.proxy.javassist;

import com.paddi.core.client.RpcReferenceWrapper;
import com.paddi.core.proxy.ProxyFactory;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:51:33
 */
public class JavassistProxyFactory implements ProxyFactory {
    public JavassistProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        Class<T> clazz = rpcReferenceWrapper.getTargetClass();
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(), clazz, new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}
