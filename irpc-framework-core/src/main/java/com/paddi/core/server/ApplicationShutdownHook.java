package com.paddi.core.server;

import com.paddi.core.common.event.RpcDestroyEvent;
import com.paddi.core.common.event.RpcListenerLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 13:28:54
 */
@Slf4j
public class ApplicationShutdownHook {
    /**
     * 注册ShutdownHook JVM进程关闭时触发
     */
    public static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("ShutdownHook is running");
            RpcListenerLoader.sendEvent(new RpcDestroyEvent("destroy"));
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
