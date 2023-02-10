package com.paddi.core.common.config;

import com.paddi.core.utils.CommonUtils;
import com.paddi.core.utils.PropertiesFileUtil;
import com.paddi.core.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 12:18:00
 */
public class PropertiesLoader {
    public static Properties properties;

    private static Map<String, String> propertiesMap = new HashMap<>();

    public static void loadConfiguration() throws IOException {
        if(properties != null) {
            return;
        }
        properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
    }

    public static String getPropertiesString(String key) {
        if(checkKey(key)) {
            return null;
        }
        if(!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return propertiesMap.get(key);
    }

    public static Integer getPropertiesInteger(String key) {
        if(checkKey(key)) {
            return null;
        }
        if(!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        String value = propertiesMap.get(key);
        return value == null ? null : Integer.valueOf(value);
    }

    public static String getPropertiesStringAssertNotBlank(String key) {
        String val = getPropertiesString(key);
        if (StringUtil.isBlank(val)) {
            throw new IllegalArgumentException("config " + key + " can't be empty");
        }
        return val;
    }

    public static Integer getPropertiesIntegerAssertNotNull(String key) {
        Integer val = getPropertiesInteger(key);
        if (val == null) {
            throw new IllegalArgumentException("config " + key + " can't be empty");
        }
        return val;
    }

    public static String getPropertiesStringOrDefault(String key, String defaultValue) {
        String val = getPropertiesString(key);
        return StringUtil.isBlank(val) ? defaultValue : val;
    }

    public static Integer getPropertiesIntegerOrDefault(String key, Integer defaultValue) {
        Integer val = getPropertiesInteger(key);
        return val == null ? defaultValue : val;
    }

    private static boolean checkKey(String key) {
        return properties == null || CommonUtils.isEmpty(key);
    }
}
