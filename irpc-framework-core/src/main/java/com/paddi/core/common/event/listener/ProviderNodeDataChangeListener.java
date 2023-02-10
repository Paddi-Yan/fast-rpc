package com.paddi.core.common.event.listener;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.common.event.RpcNodeChangeEvent;
import com.paddi.core.registry.URL;
import com.paddi.core.registry.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.paddi.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.paddi.core.common.cache.CommonClientCache.ROUTER;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 15:23:33
 */
public class ProviderNodeDataChangeListener implements RpcListener<RpcNodeChangeEvent>{
    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = (ProviderNodeInfo) t;
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for(ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String address = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            if(address.equals(providerNodeInfo.getAddress())) {
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                ROUTER.updateWeight(url);
                break;
            }
        }
    }
}
