package com.spider.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 爬虫监视器
 */
public class SpirderWatcher implements Watcher {

    private Logger logger = LoggerFactory.getLogger(SpirderWatcher.class);
    private CuratorFramework client;
    List<String> childrenList;

    public SpirderWatcher() {
        String connStr = "192.168.239.12:2181";
        //设置重复次数，重复3次，每秒间隔1000ms
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        // 连接失效时间，默认是40s.,这个值必须在40s-50s之间
        int sessionTimeoutMs = 5000;
        // 连接超时时间
        int connectionTimeoutMs = 3000;
        //获取zk连接
        client = CuratorFrameworkFactory.newClient(connStr, 5000, 3000, retry);
        //开启连接
        client.start();

        //注册监控服务，
        // "/spider" 永久性节点下面子节点的变化情况，如果发生变化，则调用process方法
        try {
            //子节点信息
            childrenList = client.getChildren().usingWatcher(this).forPath("/spider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果发现有节点变化，就会被调用
     * 只能调用一次，如果要反复使用，需要重复注册
     */
    public void process(WatchedEvent event) {
        System.out.println(event);
        //重新注册
        try {
            List<String> newChildrenList = client.getChildren().usingWatcher(this).forPath("/spider");
            for (String node : childrenList) {
                if (!newChildrenList.contains(node)) {
                    logger.info("节点消失：{}",node);
                    System.out.println("节点消失：" + node);
                    //TODO 节点消失需要发送邮件或者发送短信提醒

                }
            }
            childrenList = newChildrenList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpirderWatcher spirderWatcher = new SpirderWatcher();
        spirderWatcher.start();
    }

    private void start() {
        while (true) {
            ;
        }
    }
}
