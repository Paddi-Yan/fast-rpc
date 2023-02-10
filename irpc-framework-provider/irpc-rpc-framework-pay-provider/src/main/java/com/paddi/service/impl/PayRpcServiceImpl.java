package com.paddi.service.impl;

import com.paddi.common.RpcService;
import com.paddi.service.pay.PayRpcService;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 10:58 上午 2022/3/19
 */
@RpcService
public class PayRpcServiceImpl implements PayRpcService {

    @Override
    public boolean doPay() {
        System.out.println("pay1");
        return true;
    }

    @Override
    public List<String> getPayHistoryByGoodNo(String goodNo) {
        System.out.println("getPayHistoryByGoodNo");
        return Arrays.asList(goodNo + "-pay-001", goodNo + "-pay-002");
    }

}
