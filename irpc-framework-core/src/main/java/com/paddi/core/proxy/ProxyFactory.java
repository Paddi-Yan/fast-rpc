package com.paddi.core.proxy;

import com.paddi.core.client.RpcReferenceWrapper;
import com.paddi.core.extension.SPI;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:45:55
 */
@SPI
public interface ProxyFactory {
    <T> T getProxy(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable;
}
