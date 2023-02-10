package com.paddi.core.common.event;

import com.paddi.core.common.event.listener.ProviderNodeDataChangeListener;
import com.paddi.core.common.event.listener.RpcListener;
import com.paddi.core.common.event.listener.ServiceDestroyListener;
import com.paddi.core.common.event.listener.ServiceUpdateListener;
import com.paddi.core.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:20:31
 */
public class RpcListenerLoader {
    private static List<RpcListener> rpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(RpcListener rpcListener) {
        rpcListenerList.add(rpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
        registerListener(new ProviderNodeDataChangeListener());
    }

    public static Class<?> getInterfaceType(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if(type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendEvent(RpcEvent rpcEvent) {
        if(CommonUtils.isEmptyList(rpcListenerList)) {
            return;
        }
        for(RpcListener rpcListener : rpcListenerList) {
            //获取泛型类型
            Class<?> type = getInterfaceType(rpcListener);
            if(type.equals(rpcEvent.getClass())) {
                eventThreadPool.execute(() -> {
                    try {
                        rpcListener.callBack(rpcEvent.getData());
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}

