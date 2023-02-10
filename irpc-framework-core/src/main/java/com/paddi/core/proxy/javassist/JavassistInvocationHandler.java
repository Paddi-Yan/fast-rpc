package com.paddi.core.proxy.javassist;

import com.paddi.core.client.RpcReferenceWrapper;
import com.paddi.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.paddi.core.common.cache.CommonClientCache.RESP_MAP;
import static com.paddi.core.common.cache.CommonClientCache.SEND_QUEUE;
import static com.paddi.core.common.constants.RpcConstants.DEFAULT_TIMEOUT;
import static com.paddi.core.common.constants.RpcConstants.TIMEOUT;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:10:51
 */
public class JavassistInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();
    public static Long timeout = DEFAULT_TIMEOUT.longValue();

    private RpcReferenceWrapper rpcReferenceWrapper;

    public JavassistInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        Object o = rpcReferenceWrapper.getAttachments().get(TIMEOUT);
        if(o != null) {
            timeout = Long.valueOf(String.valueOf(o));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getTargetClass().getName());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttachments());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());
        SEND_QUEUE.add(rpcInvocation);

        //异步请求
        if(rpcReferenceWrapper.isAsync()) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        int retryTimes = 0;
        while(System.currentTimeMillis() - beginTime < timeout || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if(object instanceof RpcInvocation) {
                RpcInvocation rpcInvokeResponse = (RpcInvocation) object;
                if(rpcInvokeResponse.getException() == null) {
                    RESP_MAP.remove(rpcInvocation.getUuid());
                    return rpcInvokeResponse.getResponse();
                }else {
                    if(rpcInvokeResponse.getRetry() == 0) {
                        RESP_MAP.remove(rpcInvocation.getUuid());
                        return rpcInvokeResponse.getResponse();
                    }
                    rpcInvocation.setResponse(null);
                    rpcInvocation.setRetry(rpcInvocation.getRetry() - 1);
                    rpcInvocation.setException(null);
                    RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                    SEND_QUEUE.add(rpcInvocation);
                    retryTimes++;
                }
            }
        }
        RESP_MAP.remove(rpcInvocation.getUuid());
        throw new TimeoutException("Waiting for response from server on client "
                + timeout + "ms,Service's name is "
                + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod()
                + ". total retry count is " + retryTimes);
    }
}
