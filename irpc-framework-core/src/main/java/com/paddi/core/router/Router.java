package com.paddi.core.router;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.extension.SPI;
import com.paddi.core.registry.URL;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 14:16:00
 */
@SPI
public interface Router {

    /**
     * 刷新路由
     * @param selector
     */
    void refreshRouter(Selector selector);

    /**
     * 获取到请求的连接通道
     * @param selector
     * @return
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     * @param url
     */
    void updateWeight(URL url);
}
