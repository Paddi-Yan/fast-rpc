package com.paddi.core.filter.server;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.ServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:37:21
 */
public class ServiceAfterFilterChain {
    private static List<ServerFilter> serverFilters = new ArrayList<>();

    public void addServerFilter(ServerFilter serverFilter) {
        serverFilters.add(serverFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for(ServerFilter serverFilter : serverFilters) {
            serverFilter.doFilter(rpcInvocation);
        }
    }
}
