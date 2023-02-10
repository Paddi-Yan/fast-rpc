package com.paddi.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * 魔数 协议版本号 整体长度 协议头长度 消息类型 消息ID 序列化算法 压缩算法
 *                              拓展头
 *                              消息体
 *      0 ~ 3      4 ~ 5        6 ~ 13        14 ~ 17        18 ~ 19     20 ~ 27      28 ~ 29        30 ~ 31
 *   +------------+---------+--------------+-------------+------------+-----------+---------------+----------+
 *   | magic code | version | total length | head length | messageType| requestId | serializeType | compress |
 *   +-------------------------------------------------------------------------------------------------------+
 *   |                                                                                                       |
 *   |                                       extension                                                       |
 *   |                                                                                                       |
 *   +-------------------------------------------------------------------------------------------------------+
 *   |                                                                                                       |
 *   |                                         payload                                                       |
 *   |                                                                                                       |
 *   |                                                                                                       |
 *   +-------------------------------------------------------------------------------------------------------+
 *
 *
 * magic number(4B)   version(2B)   total length(8B)   header length(4B)   messageType(2B)   requestId(8B) serializeType(2B) compress(2B)
 *                                                         extension
 *                                                         payload
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月10日 11:31:14
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcProtocol,
                          ByteBuf byteBuf) throws Exception {
        long timestamp = (long) rpcProtocol.getExtensions().get("time");
        //魔数
        byteBuf.writeInt(MAGIC_NUMBERS);
        //协议版本号
        byteBuf.writeShort(VERSION);
        //协议体
        byte[] payload = rpcProtocol.getPayload();
        //拓展协议头长度
        int extensionsLength = EXTENSION_OPTIONS_TIMESTAMP;
        //协议头总长度
        int headerLength = HEADER_LENGTH + extensionsLength;
        //协议头和协议体的总长度
        long totalLength = headerLength + payload.length;
        byteBuf.writeLong(totalLength);
        byteBuf.writeInt(headerLength);
        short messageType = rpcProtocol.getMessageType();
        //消息类型
        byteBuf.writeShort(messageType);
        //消息ID
        byteBuf.writeLong(rpcProtocol.getRequestId());
        //序列化算法
        byteBuf.writeShort(rpcProtocol.getSerializeType());
        //压缩算法
        byteBuf.writeShort(rpcProtocol.getCompressType());
        int index = byteBuf.writerIndex();
        //协议头拓展字段
        byteBuf.writeLong(timestamp);
        //协议体内容
        byteBuf.writeBytes(payload);
    }

}
