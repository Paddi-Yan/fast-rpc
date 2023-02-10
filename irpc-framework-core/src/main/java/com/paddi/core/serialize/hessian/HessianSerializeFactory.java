package com.paddi.core.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.paddi.core.serialize.SerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 09:47:52
 */
public class HessianSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        byte[] data = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(t);
            return byteArrayOutputStream.toByteArray();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)){
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            Object object = hessianInput.readObject();
            return clazz.cast(object);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
