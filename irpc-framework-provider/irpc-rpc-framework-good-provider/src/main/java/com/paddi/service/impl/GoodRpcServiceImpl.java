package com.paddi.service.impl;


import com.paddi.common.RpcService;
import com.paddi.service.good.GoodRpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author linhao
 * @Date created in 10:59 上午 2022/3/19
 */
@RpcService
public class GoodRpcServiceImpl implements GoodRpcService {

    @Override
    public boolean decreaseStock() {
        return true;
    }

    @Override
    public List<String> selectGoodsNoByUserId(String userId) {
        System.out.println("receive request, the userId is " + userId);
        ArrayList<String> result = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 3; i++) {
            result.add(userId + "-good-" + random.nextInt());
        }
        return result;
    }
}
