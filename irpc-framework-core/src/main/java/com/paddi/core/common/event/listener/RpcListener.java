package com.paddi.core.common.event.listener;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:19:59
 */
public interface RpcListener<T> {
    void callBack(Object t);
}
