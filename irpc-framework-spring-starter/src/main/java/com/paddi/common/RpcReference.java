package com.paddi.common;

import java.lang.annotation.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 23:12:59
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcReference {
    String url() default "";

    String group() default "default";

    //TODO 版本号
    String version() default "v1.0";

    String token() default "";

    int timeout() default 3000;

    int retry() default 0;

    boolean async() default false;
}
