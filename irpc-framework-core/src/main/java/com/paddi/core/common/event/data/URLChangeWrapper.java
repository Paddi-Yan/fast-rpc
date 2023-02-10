package com.paddi.core.common.event.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:31:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class URLChangeWrapper {
    private String serviceName;

    private List<String> providerUrl;

    /**
     * 记录每个ip下边的url详细信息，包括权重，分组等
     */
    private Map<String, String> nodeDataUrl;
}
