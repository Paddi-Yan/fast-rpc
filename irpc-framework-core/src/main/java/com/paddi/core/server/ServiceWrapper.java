package com.paddi.core.server;

import lombok.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 11:12:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ServiceWrapper {
    /**
     * 对外暴露的具体对象
     */
    private Object service;
    /**
     * 对外暴露的服务默认分组
     */
    private String group;
    /**
     * 整个应用的token校验
     */
    private String token;
    /**
     * 限流策略
     */
    private Integer limit;

    private Integer weight;

}
