package com.paddi.core.filter.server;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.FilterOrder;
import com.paddi.core.filter.ServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.paddi.core.common.constants.RpcConstants.FILTER_BEFORE;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 11:08:49
 */
@FilterOrder(order = FILTER_BEFORE)
public class ServerLogFilter implements ServerFilter {
    private static Logger logger = LoggerFactory.getLogger(ServerLogFilter.class);
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        logger.info(rpcInvocation.getAttachments().get("client_application_name")
                + "do invoke" + rpcInvocation.getTargetServiceName()
                + "#" + rpcInvocation.getTargetMethod() + Arrays.toString(rpcInvocation.getArgs()));
    }
}
