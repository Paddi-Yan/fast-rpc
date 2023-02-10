package com.paddi.core.serialize;

import com.paddi.core.extension.SPI;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 09:37:17
 */
@SPI
public interface SerializeFactory {
    /**
     * 序列化
     * @param t
     * @return
     * @param <T>
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @return
     * @param <T>
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
