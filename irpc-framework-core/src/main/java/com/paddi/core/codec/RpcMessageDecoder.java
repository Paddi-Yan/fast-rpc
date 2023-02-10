package com.paddi.core.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static com.paddi.core.common.constants.RpcConstants.MAX_FRAME_LENGTH;

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
 * magic number(4B)   version(2B)   total length(8B)   head length(4B)   messageType(2B)   requestId(8B) serializeType(2B) compress(2B)
 *                                                         extension
 *                                                         payload
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月10日 11:31:14
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(MAX_FRAME_LENGTH, 6, 8, -12, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                             int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }


}
