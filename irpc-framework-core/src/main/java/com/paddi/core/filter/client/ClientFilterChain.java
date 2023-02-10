package com.paddi.core.filter.client;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.ClientFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:45:57
 */
public class ClientFilterChain {
    private static List<ClientFilter> clientFilters = new ArrayList<>();

    public void addClientFilter(ClientFilter clientFilter) {
        clientFilters.add(clientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for(ClientFilter clientFilter : clientFilters) {
            clientFilter.doFilter(src, rpcInvocation);
        }
    }
}
