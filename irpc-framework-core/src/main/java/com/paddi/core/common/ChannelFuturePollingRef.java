package com.paddi.core.common;

import com.paddi.core.router.Selector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 14:33:52
 */
public class ChannelFuturePollingRef {
    private Map<String, AtomicLong> referenceMap = new ConcurrentHashMap<>();

    public ChannelFutureWrapper getChannelFutureWrapper(Selector selector) {
        String providerServiceName = selector.getProviderServiceName();
        long i = referenceMap.computeIfAbsent(providerServiceName, key -> new AtomicLong(0))
                                        .getAndIncrement();
        ChannelFutureWrapper[] channelFutureWrappers = selector.getChannelFutureWrappers();
        int index = (int) (i % channelFutureWrappers.length);
        return channelFutureWrappers[index];
    }
}
