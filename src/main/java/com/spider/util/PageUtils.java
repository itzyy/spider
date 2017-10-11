package com.spider.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 创建工具类，主要为了需要多次初始化httpclient的时候，减少冗余代码
 */
public class PageUtils {

    /**
     * 根据url获取页面内容
     *
     * @param url
     * @return
     */
    public static String getContext(String url) {
        Logger logger = LoggerFactory.getLogger(PageUtils.class);
        //获取httpclient对象（可以任务是获取到了一个浏览器对象）
        HttpClientBuilder builder = HttpClients.custom();
        //设置代理ip,不能直接写死，建议从ip代理库获取
//        HttpHost proxy = new HttpHost("112.64.76.114", 8118);
//        CloseableHttpClient client =  builder.setProxy(proxy).build();
        CloseableHttpClient client =  builder.build();

        //封装get请求
        HttpGet httpGet = new HttpGet(url);;

        String content="";
        try {
            long startTime = System.currentTimeMillis();
            //执行请求，获取response内容
            CloseableHttpResponse response = client.execute(httpGet);
            //获取页面实体对象
            HttpEntity entity = response.getEntity();
            //只能使用一次toString ，再次使用会发生stream closed的问题
            content = EntityUtils.toString(entity);
            logger.info("页面下载成功，消耗时间：{}，url:{}",System.currentTimeMillis()-startTime,url);
        } catch (IOException e) {
            //可以在这里把失效的代理ip从本地代理库中删除掉，或者记住后，让后面的其他程序分析日志进行处理
            logger.error("页面下载失败，url:{}",url);
            e.printStackTrace();
        }
        return content;
    }
}
