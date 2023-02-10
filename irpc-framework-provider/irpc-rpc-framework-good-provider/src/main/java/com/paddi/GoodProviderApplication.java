package com.paddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 18:33:05
 */
@SpringBootApplication
public class GoodProviderApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GoodProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
