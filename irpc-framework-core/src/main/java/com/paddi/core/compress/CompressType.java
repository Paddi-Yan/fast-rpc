package com.paddi.core.compress;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wangtao .
 * @createTime on 2020/10/2
 */
@AllArgsConstructor
@Getter
public enum CompressType {

    GZIP((short) 2001, "gzip");

    private final short type;
    private final String name;

    public static short getType(String name) {
        for(CompressType value : values()) {
            if(value.getName().equals(name)) {
                return value.getType();
            }
        }
        return 0;
    }

}
