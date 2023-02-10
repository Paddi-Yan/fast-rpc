package com.paddi.core.serialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月10日 13:47:35
 */
@AllArgsConstructor
@Getter
public enum SerializeType {
    JDK((short) 1001, "jdk"),
    FASTJSON((short) 1002, "fastjson"),
    KRYO((short) 1003, "kryo"),
    HESSIAN2((short) 1004, "hessian2")
    ;

    private final short type;
    private final String name;

    public static short getType(String name) {
        for(SerializeType value : SerializeType.values()) {
            if(value.getName().equals(name)) {
                return value.getType();
            }
        }
        return 0;
    }
}
