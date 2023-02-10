package com.paddi.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.paddi.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 00:15:23
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol msg,
                          ByteBuf out) throws Exception {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
        out.writeBytes(DEFAULT_DECODE_CHAR.getBytes());
    }
}
