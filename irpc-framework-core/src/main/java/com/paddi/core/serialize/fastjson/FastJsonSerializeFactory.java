package com.paddi.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.paddi.core.serialize.SerializeFactory;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:08:40
 */
public class FastJsonSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        String s = JSON.toJSONString(t);
        return s.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(new String(bytes), clazz);
    }
}
