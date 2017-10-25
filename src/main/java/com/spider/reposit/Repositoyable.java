package com.spider.reposit;

/**
 * url仓库
 */
public interface Repositoyable {

    public void add(String nextUrl);

    public String poll();

    public void backUp(String key, String value);
}
