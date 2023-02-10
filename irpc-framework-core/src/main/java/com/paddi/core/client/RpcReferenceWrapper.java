package com.paddi.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 11:23:05
 */
public class RpcReferenceWrapper<T> {
    private Class<T> targetClass;
    private Map<String, Object> attachments = new ConcurrentHashMap<>();

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * 设置容错策略
     *
     * @param tolerant
     */
    public void setTolerant(String tolerant){
        this.attachments.put("tolerant",tolerant);
    }

    /**
     * 失败重试次数
     */
    public int getRetry(){
        if(attachments.get("retry") == null){
            return 0;
        }else {
            return (int) attachments.get("retry");
        }
    }

    public void setRetry(int retry){
        this.attachments.put("retry",retry);
    }

    public boolean isAsync() {
        Object r = attachments.get("async");
        if (r == null || r.equals(false)) {
            return false;
        }
        return Boolean.valueOf(true);
    }

    public void setAsync(boolean async) {
        this.attachments.put("async", async);
    }

    public String getUrl() {
        return String.valueOf(attachments.get("url"));
    }

    public void setUrl(String url) {
        attachments.put("url", url);
    }

    public void setTimeOut(int timeout) {
        attachments.put("timeout", timeout);
    }

    public String getTimeout() {
        return String.valueOf(attachments.get("timeout"));
    }

    public String getToken() {
        return String.valueOf(attachments.get("token"));
    }

    public void setToken(String token) {
        attachments.put("token", token);
    }

    public String getGroup() {
        return String.valueOf(attachments.get("group"));
    }

    public void setGroup(String group) {
        attachments.put("group", group);
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }
}
