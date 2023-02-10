package com.paddi.config;

import com.paddi.common.RpcService;
import com.paddi.core.common.event.RpcListenerLoader;
import com.paddi.core.server.ApplicationShutdownHook;
import com.paddi.core.server.Server;
import com.paddi.core.server.ServiceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 12:00:45
 */
@Slf4j
public class RpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Server server = null;
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(beanMap.size() == 0) {
            return;
        }
        printBanner();
        long begin = System.currentTimeMillis();
        server = new Server();
        server.initServerConfig();
        RpcListenerLoader rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();
        for(Map.Entry<String, Object> entry : beanMap.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            ServiceWrapper serviceWrapper = ServiceWrapper.builder()
                                                 .service(bean)
                                                 .group(rpcService.group())
                                                 .token(rpcService.token())
                                                 .limit(rpcService.limit())
                                                 .weight(rpcService.weight())
                                                 .token(rpcService.token()).build();
            server.exportService(serviceWrapper);
            log.info(">>>>>>>>>>>>>>> rpc server {} export success! >>>>>>>>>>>>>>>", beanName);
            log.info("bean detail information {}", serviceWrapper);
        }
        long end = System.currentTimeMillis();
        //TODO 优雅停机
        ApplicationShutdownHook.registerShutdownHook();
        server.startApplication();
        log.info(" ================== [{}] started success in {}s ================== ",
                server.getServerConfig().getApplicationName(),
                ((double)end-(double)begin)/1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printBanner(){
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- Rpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("Github: https://github.com/Paddi-Yan");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}
