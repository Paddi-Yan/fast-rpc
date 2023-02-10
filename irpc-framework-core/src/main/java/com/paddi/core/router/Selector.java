package com.paddi.core.router;

import com.paddi.core.common.ChannelFutureWrapper;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 14:18:55
 */
public class Selector {
    private String providerServiceName;

    /**
     * 经过二次筛选之后的future集合
     */
    private ChannelFutureWrapper[] channelFutureWrappers;

    public ChannelFutureWrapper[] getChannelFutureWrappers() {
        return channelFutureWrappers;
    }

    public void setChannelFutureWrappers(ChannelFutureWrapper[] channelFutureWrappers) {
        this.channelFutureWrappers = channelFutureWrappers;
    }

    public Selector() {
    }

    public Selector(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }
}
