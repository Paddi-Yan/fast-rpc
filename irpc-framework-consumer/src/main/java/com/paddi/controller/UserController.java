package com.paddi.controller;

import com.paddi.common.RpcReference;
import com.paddi.service.good.GoodRpcService;
import com.paddi.service.user.UserRpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月09日 18:11:02
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @RpcReference()
    private UserRpcService userRpcService;

    /**
     * 验证各类参数配置是否异常
     */
    @RpcReference(timeout = 10000)
    private GoodRpcService goodRpcService;

    @GetMapping(value = "/getUserId")
    public String test(){
        return userRpcService.getUserId();
    }

    @GetMapping(value = "/getGoods")
    public Object goodInvoke() {
        return goodRpcService.selectGoodsNoByUserId("123");
    }

}
