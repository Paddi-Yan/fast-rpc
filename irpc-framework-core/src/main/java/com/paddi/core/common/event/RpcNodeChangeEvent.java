package com.paddi.core.common.event;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 15:21:08
 */
public class RpcNodeChangeEvent implements RpcEvent {
    private Object data;

    public RpcNodeChangeEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public RpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
