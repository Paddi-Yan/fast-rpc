package com.paddi.core.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月07日 10:52:45
 */
@Slf4j
public class CuratorZookeeperClient extends AbstractZookeeperClient{

    private CuratorFramework client;

    public CuratorZookeeperClient(String zkAddress) {
        this(zkAddress, null, null);
    }

    public CuratorZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        super(zkAddress, baseSleepTimes, maxRetryTimes);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(super.getBaseSleepTimes(), super.getMaxRetryTimes());
        if (client == null) {
            client = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
            client.start();
        }
    }

    @Override
    public void updateNodeData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getClient() {
        return client;
    }

    @Override
    public String getNodeData(String address) {
        try {
            byte[] result = client.getData().forPath(address);
            if (result != null) {
                return new String(result);
            }
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getChildrenData(String path) {
        try {
            List<String> childrenData = client.getChildren().forPath(path);
            return childrenData;
        } catch (KeeperException.NoNodeException e) {
            log.warn("not available server provider on path {}", path);
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createPersistentData(String address, String data) {
        try {
            client.create()
                  .creatingParentContainersIfNeeded()
                  .withMode(CreateMode.PERSISTENT)
                  .forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPersistentWithSeqData(String address, String data) {
        try {
            client.create()
                  .creatingParentContainersIfNeeded()
                  .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                  .forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporarySeqData(String address, String data) {
        try {
            client.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(address, data.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporaryData(String address, String data) {
        try {
            client.create()
                  .creatingParentContainersIfNeeded()
                  .withMode(CreateMode.EPHEMERAL)
                  .forPath(address, data.getBytes());
        } catch (KeeperException.NoChildrenForEphemeralsException e) {
            try {
                client.setData().forPath(address, data.getBytes());
            } catch (Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setTemporaryData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes());
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void destroy() {
        client.close();
    }

    @Override
    public List<String> listNode(String address) {
        try {
            return client.getChildren().forPath(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean deleteNode(String address) {
        try {
            client.delete().forPath(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean existNode(String address) {
        try {
            Stat stat = client.checkExists().forPath(address);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void watchNodeData(String path, Watcher watcher) {
        try {
            client.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void watchChildNodeData(String path, Watcher watcher) {
        try {
            client.getChildren().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}