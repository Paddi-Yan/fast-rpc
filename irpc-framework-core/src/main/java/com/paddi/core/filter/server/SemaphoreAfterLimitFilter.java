package com.paddi.core.filter.server;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.ServiceSemaphoreWrapper;
import com.paddi.core.filter.FilterOrder;
import com.paddi.core.filter.ServerFilter;

import static com.paddi.core.common.cache.CommonServerCache.SERVICE_SEMAPHORE_MAP;
import static com.paddi.core.common.constants.RpcConstants.FILTER_AFTER;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:33:06
 */
@FilterOrder(order = FILTER_AFTER)
public class SemaphoreAfterLimitFilter implements ServerFilter {
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String targetServiceName = rpcInvocation.getTargetServiceName();
        ServiceSemaphoreWrapper serviceSemaphoreWrapper = SERVICE_SEMAPHORE_MAP.get(targetServiceName);
        serviceSemaphoreWrapper.getSemaphore().release();
    }
}
