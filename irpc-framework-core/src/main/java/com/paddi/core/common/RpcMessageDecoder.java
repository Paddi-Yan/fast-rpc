package com.paddi.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.paddi.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:16:04
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {
    private static final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
                          List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            if (!(byteBuf.readShort() == MAGIC_NUMBER)) {
                ctx.close();
                return;
            }
            int length = byteBuf.readInt();
            if (byteBuf.readableBytes() < length) {
                //数据包有异常
                ctx.close();
                throw new RuntimeException("RpcMessageDecoder happen exception");
            }
            byte[] body = new byte[length];
            byteBuf.readBytes(body);
            RpcProtocol rpcProtocol = new RpcProtocol(body);
            out.add(rpcProtocol);
        }
    }
}
