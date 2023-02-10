package com.paddi.core.filter.server;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.FilterOrder;
import com.paddi.core.filter.ServerFilter;
import com.paddi.core.server.ServiceWrapper;
import com.paddi.core.utils.CommonUtils;

import static com.paddi.core.common.cache.CommonClientCache.RESP_MAP;
import static com.paddi.core.common.cache.CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP;
import static com.paddi.core.common.constants.RpcConstants.FILTER_BEFORE;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 11:11:59
 */
@FilterOrder(order = FILTER_BEFORE)
public class ServerTokenFilter implements ServerFilter {
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String clientToken = String.valueOf(rpcInvocation.getAttachments().get("token"));
        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String serviceToken = serviceWrapper.getToken();
        if(CommonUtils.isEmpty(serviceToken)) {
            return;
        }
        if(!CommonUtils.isEmpty(clientToken) && clientToken.equals(serviceToken)) {
            return;
        }
        rpcInvocation.setRetry(0);
        rpcInvocation.setException(new RuntimeException("token [" + clientToken + "] verify result is false!"));
        rpcInvocation.setResponse(null);
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        throw new RuntimeException("token [" + clientToken + "] verify result is false!");
    }
}
