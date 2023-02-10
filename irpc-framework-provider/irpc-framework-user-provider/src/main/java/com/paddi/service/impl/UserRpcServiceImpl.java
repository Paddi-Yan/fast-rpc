package com.paddi.service.impl;


import com.paddi.common.RpcReference;
import com.paddi.common.RpcService;
import com.paddi.service.good.GoodRpcService;
import com.paddi.service.pay.PayRpcService;
import com.paddi.service.user.UserRpcService;

import java.util.*;

/**
 * @Author linhao
 * @Date created in 10:07 上午 2022/3/19
 */
@RpcService
public class UserRpcServiceImpl implements UserRpcService {

    @RpcReference
    private GoodRpcService goodRpcService;
    @RpcReference
    private PayRpcService payRpcService;

    @Override
    public String getUserId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<Map<String, String>> findMyGoods(String userId) {
        List<String> goodsNoList = goodRpcService.selectGoodsNoByUserId(userId);
        List<Map<String, String>> finalResult = new ArrayList<>();
        goodsNoList.forEach(goodsNo -> {
            Map<String, String> item = new HashMap<>(2);
            List<String> payHistory = payRpcService.getPayHistoryByGoodNo(goodsNo);
            item.put(goodsNo, payHistory.toString());
            finalResult.add(item);
        });
        return finalResult;
    }
}
