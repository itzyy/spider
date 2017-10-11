package com.spider.process;

/**
 * Created by Administrator on 2017/10/6.
 */
public interface Processable<T> {

    public void process(T page);
}
