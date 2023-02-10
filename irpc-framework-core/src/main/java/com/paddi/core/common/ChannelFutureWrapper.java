package com.paddi.core.common;

import io.netty.channel.ChannelFuture;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:43:41
 */
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    private Integer weight;

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public ChannelFutureWrapper() {
    }

    public ChannelFutureWrapper(String host, Integer port, Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public String getHost() {
        return host;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
