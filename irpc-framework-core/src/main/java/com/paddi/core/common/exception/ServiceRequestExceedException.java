package com.paddi.core.common.exception;

import com.paddi.core.common.RpcInvocation;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:29:00
 */
public class ServiceRequestExceedException extends RpcException{
    public ServiceRequestExceedException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
