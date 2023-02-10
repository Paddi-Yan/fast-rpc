package com.paddi.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 23:14:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    int limit() default 100;

    String group() default "default";

    int weight() default 100;

    String token() default "";
}
