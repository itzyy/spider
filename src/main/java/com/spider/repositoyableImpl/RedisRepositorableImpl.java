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

    public  void add(String nextUrl) {
        this.redisUtils.add_s(RedisUtils.key, nextUrl);
    }

    public String poll() {
        return this.redisUtils.poll_s(RedisUtils.key);
    }

    public void backUp(String key,String value) {
        this.redisUtils.add_s(RedisUtils.u_key,value);
    }
}
