package com.paddi.core.codec;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月10日 13:41:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RpcInvocation implements Serializable {
    private static final long serialVersionUID = -2339702023989759560L;
    /**
     * 请求的目标方法
     */
    private String targetMethod;
    /**
     * 请求的目标服务名称
     */
    private String targetServiceName;
    /**
     * 请求参数信息
     */
    private Object[] args;

    /**
     * 接口响应的数据塞入这个字段中（如果是异步调用或者void类型，这里就为空）
     */
    private Object response;

    private Throwable exception;

    private short messageType;

    private long requestId;

    private int retry;

    private Map<String, Object> attachments = new ConcurrentHashMap<>();
}
