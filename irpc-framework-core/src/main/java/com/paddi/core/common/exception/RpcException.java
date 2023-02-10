package com.paddi.core.common.exception;

import com.paddi.core.common.RpcInvocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 22:26:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcException extends RuntimeException{
    private RpcInvocation rpcInvocation;
}
