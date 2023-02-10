package com.paddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 18:40:51
 */
@SpringBootApplication
public class PayProviderApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PayProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
