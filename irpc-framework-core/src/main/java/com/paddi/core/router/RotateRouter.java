package com.paddi.core.router;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.registry.URL;

import java.util.List;

import static com.paddi.core.common.cache.CommonClientCache.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 15:33:50
 */
public class RotateRouter implements Router{
    @Override
    public void refreshRouter(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] wrapperArray = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for(int i = 0; i < channelFutureWrappers.size(); i++) {
            wrapperArray[i] = channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), wrapperArray);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector);
    }

    @Override
    public void updateWeight(URL url) {

    }
}
