package com.ydw;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.security.acl.Acl;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author ydw
 * Created on 2018/5/30
 */
public class ZookeeperDemo {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(0);
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                countDownLatch.countDown();
                System.out.println(watchedEvent.getType());
            }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
        zooKeeper.create("/test","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,new MyStringCallBack(),"回传内容");
        Thread.sleep(2000);
        zooKeeper.exists("/ddd",true);
        zooKeeper.create("/ddd","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        Thread.sleep(2000);
        zooKeeper.exists("/ddd",true);
        zooKeeper.delete("/ddd",-1);

    }
}

class MyStringCallBack implements AsyncCallback.StringCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("异步创建回调结果：状态：" + rc +"；创建路径：" +
                path + "；传递信息：" + ctx + "；实际节点名称：" + name);
    }
}