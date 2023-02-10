package com.paddi.core.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.paddi.core.serialize.SerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 10:04:24
 */
public class KryoSerializeFactory implements SerializeFactory {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        return kryo;
    });


    @Override
    public <T> byte[] serialize(T t) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, t);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
