package com.spider.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/6.
 */
public class Page {

    /**
     * 页面内容
     */
    private String content;

    /**
     * url
     */
    private String url;

    /**
     * 商品编号

     */
    private String productNo;

    /**
     * 商品列表
     */
    private List<String> urls = new ArrayList<String>();

    private Map<String,String> values = new HashMap<String, String>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addField(String key,String value){
        values.put(key,value);
    }
    public String getProductNo() {
        return productNo;
    }
    public void addUrl(String url){
        this.urls.add(url);
    }
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public List<String> getUrls() {
        return urls;
    }


}
