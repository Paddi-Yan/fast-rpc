package com.paddi.core.filter.server;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.ServiceSemaphoreWrapper;
import com.paddi.core.common.exception.ServiceRequestExceedException;
import com.paddi.core.filter.FilterOrder;
import com.paddi.core.filter.ServerFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static com.paddi.core.common.cache.CommonServerCache.SERVICE_SEMAPHORE_MAP;
import static com.paddi.core.common.constants.RpcConstants.FILTER_BEFORE;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:17:13
 */
@FilterOrder(order = FILTER_BEFORE)
@Slf4j
public class SemaphoreBeforeLimitFilter implements ServerFilter {
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String targetServiceName = rpcInvocation.getTargetServiceName();
        ServiceSemaphoreWrapper serviceSemaphoreWrapper = SERVICE_SEMAPHORE_MAP.get(targetServiceName);
        Semaphore semaphore = serviceSemaphoreWrapper.getSemaphore();
        boolean tryResult = semaphore.tryAcquire();
        if(!tryResult) {
            log.error("{}'s max request limit is {}, request is rejected now", targetServiceName, serviceSemaphoreWrapper.getLimit());
            ServiceRequestExceedException exception = new ServiceRequestExceedException(rpcInvocation);
            rpcInvocation.setException(exception);
            throw exception;
        }
    }
}
