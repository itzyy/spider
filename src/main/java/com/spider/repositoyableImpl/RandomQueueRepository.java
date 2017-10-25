package com.spider.repositoyableImpl;

import com.spider.reposit.Repositoyable;
import com.spider.util.DomainUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2017/10/7.
 */
public class RandomQueueRepository implements Repositoyable {

    private Map<String,Queue<String>> map  =new HashMap<String, Queue<String>>();
    private Random random = new Random();

    /**
     * 添加各个网站map数据,数据结构 ,map.put("www.jd.com",url队列)
     * @param nextUrl
     */
    public void add(String nextUrl) {
        //获取顶级域名
        String topDomain = DomainUtils.getTopDomain(nextUrl);
        Queue<String> queue = map.get(topDomain);
        if(queue==null){
            queue = new ConcurrentLinkedQueue<String>();
        }
        queue.add(nextUrl);
        map.put(topDomain,queue);
    }

    public String poll() {
        String[] keys = map.keySet().toArray(new String[map.size()]);
        // 随机获取一个数字，这个数字就是数组的角标
        int nextInt = random.nextInt(keys.length);
        Queue<String> queue = map.get(keys[nextInt]);
        return queue.poll();
    }

    @Override
    public void backUp(String key, String value) {

    }
}
