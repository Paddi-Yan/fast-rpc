package com.paddi.core.filter.client;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.ClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.paddi.core.common.cache.CommonClientCache.CLIENT_CONFIG;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:55:07
 */
public class ClientLogFilter implements ClientFilter {
    private static Logger logger = LoggerFactory.getLogger(ClientLogFilter.class);
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("client_application_name", CLIENT_CONFIG.getApplicationName());
        logger.info(rpcInvocation.getAttachments().get("client_application_name") + "do invoke to" + rpcInvocation.getTargetServiceName());
    }
}
