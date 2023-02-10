package com.paddi.config;

import com.paddi.core.client.Client;
import com.paddi.core.client.ConnectionHandler;
import com.paddi.core.client.RpcReference;
import com.paddi.core.client.RpcReferenceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 17:37:53
 */
@Slf4j
public class RpcClientAutoConfiguration implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {
    private static RpcReference rpcReference;
    private static Client client;
    private volatile boolean needInitClient = false;
    private volatile boolean hasInitClientConfig = false;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        //遍历字段属性
        for(Field field : fields) {
            //获取远程调用的接口字段
            if(field.isAnnotationPresent(com.paddi.common.RpcReference.class)) {
                if(!hasInitClientConfig) {
                    client = new Client();
                    try {
                        rpcReference = client.startClientApplication();
                    } catch(InterruptedException e) {
                        log.error("postProcessAfterInitialization init error");
                        throw new RuntimeException(e);
                    }
                    hasInitClientConfig = true;
                }
                needInitClient = true;
                //获取注解
                com.paddi.common.RpcReference annotation = field.getAnnotation(com.paddi.common.RpcReference.class);
                try {
                    field.setAccessible(true);
                    RpcReferenceWrapper rpcReferenceWrapper = new RpcReferenceWrapper();
                    rpcReferenceWrapper.setTargetClass(field.getType());
                    rpcReferenceWrapper.setGroup(annotation.group());
                    rpcReferenceWrapper.setToken(annotation.token());
                    rpcReferenceWrapper.setUrl(annotation.url());
                    rpcReferenceWrapper.setTimeOut(annotation.timeout());
                    rpcReferenceWrapper.setRetry(annotation.retry());
                    rpcReferenceWrapper.setAsync(annotation.async());
                    //生成代理对象
                    Object proxy = rpcReference.get(rpcReferenceWrapper);
                    field.set(bean, proxy);
                    client.doSubscribeService(field.getType());
                }catch(Exception e) {
                    log.error("postProcessAfterInitialization init error: {}", e);
                } catch(Throwable e) {
                    log.error("init proxy error: {}", e);
                }
            }
        }
        return bean;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (needInitClient && client!=null) {
            log.info(" ================== [{}] started success! ================== ",client.getClientConfig().getApplicationName());
            ConnectionHandler.setBootstrap(client.getBootstrap());
            client.doConnectServer();
            client.startClient();
        }
    }
}
