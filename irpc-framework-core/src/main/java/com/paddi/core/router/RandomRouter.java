package com.paddi.core.router;

import com.paddi.core.common.ChannelFutureWrapper;
import com.paddi.core.registry.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.paddi.core.common.cache.CommonClientCache.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 14:24:20
 */
public class RandomRouter implements Router{
    @Override
    public void refreshRouter(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] wrapperArray = new ChannelFutureWrapper[channelFutureWrappers.size()];
        //提前生成调用先后顺序的随机数组
        int[] result = createRandomIndex(wrapperArray.length);
        //生成对应服务集群的每台机器调用顺序
        for(int i = 0; i < result.length; i++) {
            wrapperArray[i] = channelFutureWrappers.get(result[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), wrapperArray);
        //TODO 更新权重
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector);
    }

    @Override
    public void updateWeight(URL url) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(url.getServiceName());
        Integer[] weightArray = createWeightArray(channelFutureWrappers);
        Integer[] finalArray = createRandomArray(weightArray);
        ChannelFutureWrapper[] finalChannelFutureWrappers = new ChannelFutureWrapper[finalArray.length];
        for(int i = 0; i < finalArray.length; i++) {
            finalChannelFutureWrappers[i] = channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(url.getServiceName(), finalChannelFutureWrappers);
    }

    private static Integer[] createWeightArray(List<ChannelFutureWrapper> channelFutureWrappers) {
        ArrayList<Integer> weights = new ArrayList<>();
        for(int k = 0; k < channelFutureWrappers.size(); k++) {
            Integer weight = channelFutureWrappers.get(k).getWeight();
            int c = weight / 100;
            for(int i = 0; i < c; i++) {
                weights.add(k);
            }
        }
        return weights.toArray(new Integer[weights.size()]);
    }


    /**
     * 创建随机乱序数组
     *
     * @param arr
     * @return
     */
    private static Integer[] createRandomArray(Integer[] arr) {
        int total = arr.length;
        Random ra = new Random();
        for (int i = 0; i < total; i++) {
            int j = ra.nextInt(total);
            if (i == j) {
                continue;
            }
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    /**
     * 创建随机乱序数组
     * @param length
     * @return
     */
    private int[] createRandomIndex(int length) {
        int[] arrInt = new int[length];
        Random random = new Random();
        for(int i = 0; i < arrInt.length; i++) {
            arrInt[i] = -1;
        }
        int index = 0;
        while(index < arrInt.length) {
            int num = random.nextInt(length);
            if(!contains(arrInt, num)) {
                arrInt[index++] = num;
            }
        }
        return arrInt;
    }

    public boolean contains(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * 测试代码片段
     */
    public static void main(String[] args) {
        List<ChannelFutureWrapper> channelFutureWrappers = new ArrayList<>();
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 100));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 200));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 500));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 400));
        Integer[] r = createWeightArray(channelFutureWrappers);
        for(Integer integer : r) {
            System.out.println(integer);
        }
    }


}
