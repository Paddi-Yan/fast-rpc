package com.paddi.core.codec;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

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
@Data
@ToString
@Builder
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = -7433392097542011576L;
    private short version;
    private int headLength;
    private short messageType;
    private long requestId;
    private short serializeType;
    private short compressType;
    private byte[] payload;
    private Map<String, Object> extensions;

    public RpcProtocol() {
        extensions.put("time", System.currentTimeMillis());
    }
}
