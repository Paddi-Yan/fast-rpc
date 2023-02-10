package com.paddi.core.common.event;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:18:43
 */
public interface RpcEvent {
    Object getData();

    RpcEvent setData(Object data);
}
