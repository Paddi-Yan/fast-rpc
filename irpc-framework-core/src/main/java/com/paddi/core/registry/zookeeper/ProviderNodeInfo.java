package com.paddi.core.registry.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:36:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProviderNodeInfo {
    private String applicationName;

    private String serviceName;

    private String address;

    private Integer weight;

    private String registryTime;

    private String group;
}
