package com.spider.repositoyableImpl;

import com.spider.reposit.Repositoyable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2017/10/7.
 */
public class QueueRepositorableImpl implements Repositoyable {
    private Queue<String> queue = new ConcurrentLinkedQueue<String>();

    public void add(String nextUrl) {
        this.queue.add(nextUrl);
    }

    public String poll() {
        return this.queue.poll();
    }

    @Override
    public void backUp(String key, String value) {

    }
}
