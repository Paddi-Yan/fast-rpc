package com.paddi.core.filter;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.extension.SPI;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:44:12
 */
@SPI
public interface ServerFilter extends Filter{
    /**
     * 执行核心过滤逻辑
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);
}
