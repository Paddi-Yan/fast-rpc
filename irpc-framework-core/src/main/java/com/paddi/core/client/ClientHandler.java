package com.paddi.core.client;

import com.paddi.core.common.RpcInvocation;
import com.paddi.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import static com.paddi.core.common.cache.CommonClientCache.CLIENT_SERIALIZE_FACTORY;
import static com.paddi.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:42:17
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //客户端和服务端之间的数据都是以RpcProtocol对象作为
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        byte[] content = rpcProtocol.getContent();
        RpcInvocation rpcInvocation = CLIENT_SERIALIZE_FACTORY.deserialize(content, RpcInvocation.class);
        log.info("RpcInvocation RequestId: {}", rpcInvocation.getUuid());
        if(rpcInvocation.getException() != null) {
            log.error("remote invoke exception, detail info: targetInvokeService = [{}], method = [{}], exception", rpcInvocation.getTargetServiceName(), rpcInvocation.getTargetMethod(), rpcInvocation.getException());
        }
        if(!RESP_MAP.containsKey(rpcInvocation.getUuid())) {
            throw new IllegalArgumentException("server response is error!");
        }
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
