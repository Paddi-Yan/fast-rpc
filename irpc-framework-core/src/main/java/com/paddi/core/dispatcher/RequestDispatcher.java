package com.paddi.core.dispatcher;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.RpcProtocol;
import com.paddi.core.common.exception.RpcException;
import com.paddi.core.server.RequestWrapper;
import com.paddi.core.utils.threadpool.CustomThreadPoolConfig;
import com.paddi.core.utils.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.paddi.core.common.cache.CommonServerCache.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 16:33:42
 */
@Slf4j
public class RequestDispatcher {
    private BlockingQueue<RequestWrapper> requestQueue;
    private ExecutorService executorService;

    public void init(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueMaxSize) {
        requestQueue = new ArrayBlockingQueue<>(queueMaxSize);
        CustomThreadPoolConfig threadPoolConfig = new CustomThreadPoolConfig();
        threadPoolConfig.setCorePoolSize(corePoolSize);
        threadPoolConfig.setMaximumPoolSize(maximumPoolSize);
        threadPoolConfig.setKeepAliveTime(keepAliveTime);
        threadPoolConfig.setUnit(unit);
        threadPoolConfig.setWorkQueue(new ArrayBlockingQueue<>(queueMaxSize));
        executorService = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("rpc-thread-pool", threadPoolConfig);
    }

    public void add(RequestWrapper requestWrapper) {
        requestQueue.add(requestWrapper);
    }

    public void startRequestConsumer() {
        new Thread(new RequestHandler()).start();
    }

    class RequestHandler implements Runnable {

        @Override
        public void run() {
            while(true) {
                try {
                    RequestWrapper requestWrapper = requestQueue.take();
                    executorService.submit(() -> {
                        RpcProtocol rpcProtocol = requestWrapper.getRpcProtocol();
                        RpcInvocation rpcInvocation = null;
                        try {
                            rpcInvocation = SERVER_SERIALIZE_FACTORY.deserialize(rpcProtocol.getContent(), RpcInvocation.class);
                            //执行过滤链
                            try {
                                SERVICE_BEFORE_FILTER_CHAIN.doFilter(rpcInvocation);
                            }catch(Exception e) {
                                //针对自定义异常进行捕获
                                if(e instanceof RpcException) {
                                    RpcException rpcException = (RpcException) e;
                                    RpcInvocation failedRpcInvocation = rpcException.getRpcInvocation();
                                    rpcInvocation.setException(rpcException);
                                    byte[] failedResponse = SERVER_SERIALIZE_FACTORY.serialize(rpcException);
                                    RpcProtocol failedRpcProtocol = new RpcProtocol(failedResponse);
                                    requestWrapper.getChannelHandlerContext().writeAndFlush(failedRpcProtocol);
                                    return;
                                }
                            }
                            Object targetService = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
                            Method[] methods = targetService.getClass().getDeclaredMethods();
                            Object result = null;
                            for(Method method : methods) {
                                if(method.getName().equals(rpcInvocation.getTargetMethod())) {
                                    if(method.getReturnType().equals(Void.TYPE)) {
                                        try {
                                            method.invoke(targetService, rpcInvocation.getArgs());
                                        } catch(Exception e) {
                                            //业务调用异常
                                            rpcInvocation.setException(e);
                                        }
                                    }else {
                                        try {
                                            result = method.invoke(targetService, rpcInvocation.getArgs());
                                        } catch(Exception e) {
                                            //业务调用异常
                                            rpcInvocation.setException(e);
                                        }
                                    }
                                    break;
                                }
                            }
                            rpcInvocation.setResponse(result);
                            //后置过滤器
                            SERVICE_AFTER_FILTER_CHAIN.doFilter(rpcInvocation);
                            RpcProtocol responseRpcProtocol = new RpcProtocol(SERVER_SERIALIZE_FACTORY.serialize(rpcInvocation));
                            requestWrapper.getChannelHandlerContext().writeAndFlush(responseRpcProtocol);
                            RequestDispatcher.log.info("method {} invoke successfully", rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
                        } catch(Exception e) {
                            e.printStackTrace();
                            RequestDispatcher.log.error("method [{}] invoke fail, cause: [{}]", rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod(), e);
                        }
                    });
                }catch(Exception e) {
                    RequestDispatcher.log.error("request handler[{}] fail, cause: [{}]", requestQueue, e);
                }
            }
        }
    }
}
