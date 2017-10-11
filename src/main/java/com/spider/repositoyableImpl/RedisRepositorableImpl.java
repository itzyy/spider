package com.spider.repositoyableImpl;

import com.spider.reposit.Repositoyable;
import com.spider.util.RedisUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2017/10/7.
 */
public class RedisRepositorableImpl implements Repositoyable {
    private RedisUtils redisUtils = new RedisUtils();

    public void add(String nextUrl) {
        this.redisUtils.add(RedisUtils.key, nextUrl);
    }

    public String poll() {
        return this.redisUtils.poll(RedisUtils.key);
    }
}
