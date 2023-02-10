package com.paddi.core.filter;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.extension.SPI;

import java.util.List;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:42:43
 */
@SPI
public interface ClientFilter extends Filter{
    /**
     * 执行过滤链
     * @param src
     * @param rpcInvocation
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);
}
