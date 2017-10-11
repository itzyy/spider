import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by Administrator on 2017/10/7.
 */
public class TestCurator {

    @Test
    public void test() {
        String connStr = "192.168.239.12:2181";
        //设置重复次数，重复3次，每秒间隔1000ms
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        // 连接失效时间，默认是40s.,这个值必须在40s-50s之间
        int sessionTimeoutMs = 5000;
        // 连接超时时间
        int connectionTimeoutMs=3000;
        //获取zk连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(connStr,5000,3000,retry);
        //开启连接
        client.start();
        //创建节点
        try {
        //获取当前爬虫运行的ip
        InetAddress host = InetAddress.getLocalHost();
            client.create()
                    .creatingParentsIfNeeded()//如果父节点不存在，则创建
                    .withMode(CreateMode.EPHEMERAL) //设置节点属性，是否为临时(临时节点需要存放在永久节点下面)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)//设置节点权限
                    .forPath("/spider/"+host);//节点名称，
        } catch (Exception e) {
            e.printStackTrace();
        }
        //程序不停止，则节点一直存在
        while(true){
            ;
        }
    }
}
