package com.paddi.core.filter.client;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.ClientFilter;
import com.paddi.core.utils.CommonUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.paddi.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:59:12
 */
public class GroupClientFilter implements ClientFilter {
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = (String) rpcInvocation.getAttachments().get("group");
        src = src.stream()
                .filter(channelFutureWrapper -> channelFutureWrapper != null && channelFutureWrapper.getGroup().equals(group))
                .collect(Collectors.toList());
        if(CommonUtils.isEmptyList(src)) {
            rpcInvocation.setException(new RuntimeException("no provider match for group " + group));
            rpcInvocation.setResponse(null);
            rpcInvocation.setRetry(0);
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for group " + group);
        }
    }
}
