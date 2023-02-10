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
 * @CreatedTime: 2023年02月08日 11:01:23
 */
public class DirectInvokeFilter implements ClientFilter {
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if(CommonUtils.isEmpty(url)) {
            return;
        }
        src = src.stream()
                .filter(channelFutureWrapper -> url.equals(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort()))
                .collect(Collectors.toList());
        if(CommonUtils.isEmptyList(src)) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setException(new RuntimeException("no match provider url for " + url));
            rpcInvocation.setResponse(null);
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no match provider url for " + url);
        }
    }
}
