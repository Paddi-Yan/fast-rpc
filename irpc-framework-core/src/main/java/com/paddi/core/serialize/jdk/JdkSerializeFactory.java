package com.paddi.core.serialize.jdk;

import com.paddi.core.serialize.SerializeFactory;

import java.io.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 09:39:53
 */
public class JdkSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        byte[] data = null;
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            data = byteArrayOutputStream.toByteArray();
            return data;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
            Object object = objectInputStream.readObject();
            return (T) object;
        } catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
