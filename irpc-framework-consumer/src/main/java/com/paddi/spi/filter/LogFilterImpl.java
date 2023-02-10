package com.paddi.spi.filter;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.RpcInvocation;
import com.paddi.core.filter.ClientFilter;

import java.util.List;

/**
 * @Author linhao
 * @Date created in 4:31 下午 2022/2/4
 */
public class LogFilterImpl implements ClientFilter {
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        System.out.println("this is a test");
    }
}
