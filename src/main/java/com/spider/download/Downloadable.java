package com.spider.download;

import java.io.Serializable;

/**
 * 抽取接口类，主要考虑当实现方式方法变化时，可以用接口进行调换，如将httpclient转换成其他方式
 */
public interface Downloadable<T extends Serializable> {

    /**
     * 下载功能
     * @param url   下载连接
     */
    public T download(String url);
}
