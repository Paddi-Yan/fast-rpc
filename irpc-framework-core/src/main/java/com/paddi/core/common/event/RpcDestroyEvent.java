package com.paddi.core.common.event;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 15:27:32
 */
public class RpcDestroyEvent implements RpcEvent{
    private Object data;

    public RpcDestroyEvent(Object data) {
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
