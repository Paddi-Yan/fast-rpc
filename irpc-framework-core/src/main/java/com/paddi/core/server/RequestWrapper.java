package com.paddi.core.server;

import com.paddi.core.common.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 16:34:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestWrapper {
    private RpcProtocol rpcProtocol;
    private ChannelHandlerContext channelHandlerContext;

}
