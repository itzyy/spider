package com.spider.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zouyy on 2017/10/11.
 */
public class PoiPage extends Page {

    /**
     * POI数据
     */
    private String url;
    /**
     * 页面内容
     */
    private String context;

    private List<String> urls = new ArrayList<String>();
    /**
     * 第四层具体内容
     */
    private Map<String, String> params = new HashMap<String, String>();


    public void addField(String key, String value) {
        params.put(key, value);
    }

    public String getValue(String key) {
        return params.get(key);
    }

    public void setAdd(String value){
        this.urls.add(value);
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<String> getUrls() {
        return urls;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                this.getValue("head"),
                this.getValue("province"),
                this.getValue("distinct"),
                this.getValue("address"),
                this.getValue("phoneNo"),
                this.getValue("tag"),
                this.getValue("eCoords"),
                this.getValue("mCoords"),
                this.getValue("bCoords")
        );
    }
}
