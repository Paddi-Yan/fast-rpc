package com.paddi.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.paddi.core.common.event.RpcEvent;
import com.paddi.core.common.event.RpcListenerLoader;
import com.paddi.core.common.event.RpcNodeChangeEvent;
import com.paddi.core.common.event.RpcUpdateEvent;
import com.paddi.core.common.event.data.URLChangeWrapper;
import com.paddi.core.registry.AbstractRegister;
import com.paddi.core.registry.URL;
import com.paddi.core.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paddi.core.common.cache.CommonClientCache.CLIENT_CONFIG;
import static com.paddi.core.common.cache.CommonServerCache.SERVER_CONFIG;
import static com.paddi.core.common.constants.RpcConstants.*;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 11:02:05
 */
@Slf4j
public class ZookeeperRegister extends AbstractRegister{

    private AbstractZookeeperClient zkClient;
    private String ROOT = "/rpc";

    public ZookeeperRegister() {
        String registryAddress = CLIENT_CONFIG != null ? CLIENT_CONFIG.getRegisterAddr() : SERVER_CONFIG.getRegisterAddr();
        this.zkClient = new CuratorZookeeperClient(registryAddress);
    }

    private String getProviderPath(URL url) {
         return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get(HOST) + ":" + url.getParameters().get(PORT);
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host")+":";
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        //监听是否有新的服务注册
        String servicePath = url.getParameters().get(URL_PARAMETER_SERVICE_PATH);
        String newServerNodePath = ROOT + "/" + servicePath;
        watchChildNodeData(newServerNodePath);
        String providerIps = url.getParameters().get(URL_PARAMETER_PROVIDER_IPS);
        List<String> providerIpList = JSON.parseObject(providerIps, List.class);
        for(String providerIp : providerIpList) {
            watchNodeDataChange(ROOT + "/" + servicePath + "/" + providerIp);
        }
    }

    /**
     * 订阅服务节点内部的数据变化
     *
     * @param newServerNodePath
     */
    private void watchNodeDataChange(String newServerNodePath) {
        zkClient.watchNodeData(newServerNodePath, watchedEvent -> {
            String path = watchedEvent.getPath();
            String nodeData = zkClient.getNodeData(path);
            ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(nodeData);
            RpcNodeChangeEvent rpcNodeChangeEvent = new RpcNodeChangeEvent(providerNodeInfo);
            RpcListenerLoader.sendEvent(rpcNodeChangeEvent);
            watchNodeDataChange(newServerNodePath);
        });
    }

    private void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, watchedEvent -> {
            String path = watchedEvent.getPath();
            log.info("receive child node {} data change", path);
            List<String> childrenNodeList = zkClient.getChildrenData(path);
            if(CommonUtils.isEmptyList(childrenNodeList)) {
                watchChildNodeData(path);
                return;
            }
            URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
            Map<String, String> nodeDetailInfoMap = new HashMap<>();
            for(String providerAddress : childrenNodeList) {
                String nodeDetailInfo = zkClient.getNodeData(path + "/" + providerAddress);
                nodeDetailInfoMap.put(providerAddress, nodeDetailInfo);
            }
            urlChangeWrapper.setNodeDataUrl(nodeDetailInfoMap);
            urlChangeWrapper.setProviderUrl(childrenNodeList);
            urlChangeWrapper.setServiceName(path.split("/")[2]);
            //自定义的一套事件监听组件
            RpcEvent rpcEvent = new RpcUpdateEvent(urlChangeWrapper);
            RpcListenerLoader.sendEvent(rpcEvent);
            //收到回调之后在注册一次监听，这样能保证一直都收到消息
            watchChildNodeData(path);
            for(String providerAddress : childrenNodeList) {
                watchNodeDataChange(path + "/" + providerAddress);
            }
        });
    }

    @Override
    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        List<String> childrenData = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        HashMap<String, String> result = new HashMap<>();
        for(String address : childrenData) {
            String clientNodeData = this.zkClient.getNodeData(ROOT + "/" + serviceName + "/provider/" + address);
            result.put(address, clientNodeData);
        }
        return result;
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        return zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
    }

    @Override
    public void register(URL url) {
        if(!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlString(url);
        String providerPath = getProviderPath(url);
        if(!zkClient.existNode(providerPath)) {
            zkClient.createTemporaryData(providerPath, urlStr);
        }else {
            zkClient.deleteNode(providerPath);
            zkClient.createTemporaryData(providerPath, urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }
    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlString(url);
        String consumerPath = getConsumerPath(url);
        if (!zkClient.existNode(consumerPath)) {
            zkClient.createTemporarySeqData(consumerPath, urlStr);
        } else {
            zkClient.deleteNode(consumerPath);
            zkClient.createTemporarySeqData(consumerPath, urlStr);
        }
        super.subscribe(url);
    }

}
