package com.paddi.core.common;

import lombok.Data;

import java.util.concurrent.Semaphore;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:22:48
 */
@Data
public class ServiceSemaphoreWrapper {
    private Semaphore semaphore;

    private int limit;

    public ServiceSemaphoreWrapper(int limit) {
        this.limit = limit;
        this.semaphore = new Semaphore(limit);
    }
}
