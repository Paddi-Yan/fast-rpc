package com.paddi.core.registry;

import com.paddi.core.registry.zookeeper.ProviderNodeInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:25:44
 */
public class URL {
    /**
     * 服务应用名称
     */
    private String applicationName;
    /**
     * 注册到节点到服务名称
     */
    private String serviceName;
    /**
     * 这里面可以自定义不限进行扩展
     * 分组
     * 权重
     * 服务提供者的地址
     * 服务提供者的端口
     */
    private Map<String, String> parameters = new HashMap<>();
    /**
     * 将URL转换为写入zk的provider节点下的一段字符串
     * @param url
     * @return
     */
    public static String buildProviderUrlString(URL url) {
        String host = url.getParameters().get(HOST);
        String port = url.getParameters().get(PORT);
        String group = url.getParameters().get(GROUP);
        String weight = url.getParameters().get(WEIGHT);
        return new String((url.getApplicationName() + ";"
                + url.getServiceName() + ";"
                + host + ":" + port + ";"
                + System.currentTimeMillis() + ";"
                + weight + ";"
                +group).getBytes(), StandardCharsets.UTF_8) ;
    }

    /**
     * 将URL转换为写入zk的consumer节点下的一段字符串
     *
     * @param url
     * @return
     */
    public static String buildConsumerUrlString(URL url) {
        String host = url.getParameters().get(HOST);
        return new String((url.getApplicationName() + ";"
                + url.getServiceName() + ";" + host
                + ";" + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将某个节点下的信息转换为一个Provider节点对象
     *
     * @param providerNodeStr
     * @return
     */
    public static ProviderNodeInfo buildURLFromUrlStr(String providerNodeStr) {
        String[] items = providerNodeStr.split(";");
        ProviderNodeInfo providerNodeInfo = new ProviderNodeInfo();
        providerNodeInfo.setApplicationName(items[0]);
        providerNodeInfo.setServiceName(items[1]);
        providerNodeInfo.setAddress(items[2]);
        providerNodeInfo.setRegistryTime(items[3]);
        providerNodeInfo.setWeight(Integer.valueOf(items[4]));
        providerNodeInfo.setGroup(String.valueOf(items[5]));
        return providerNodeInfo;
    }

    public void addParameter(String key, String value) {
        this.parameters.putIfAbsent(key, value);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}