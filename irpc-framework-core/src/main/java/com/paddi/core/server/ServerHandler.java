package com.paddi.core.server;

import com.paddi.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.paddi.core.common.cache.CommonServerCache.REQUEST_DISPATCHER;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:24:53
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestWrapper requestWrapper = new RequestWrapper((RpcProtocol) msg, ctx);
        REQUEST_DISPATCHER.add(requestWrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if(channel.isActive()) {
            ctx.close();
        }
    }
}


