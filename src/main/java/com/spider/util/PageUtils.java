package com.spider.util;

import com.spider.proxy.ProxyManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
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
    private static Logger logger = LoggerFactory.getLogger(PageUtils.class);
    private static RedisUtils redisUtils = new RedisUtils();
    private static String proxyInfo = "";

    /**
     * 根据url获取页面内容
     *
     * @param url
     * @return
     */
    public static String getContext(String url) {
        String content = "";
        try {
            //获取client对象
            CloseableHttpClient client = getHttpClient();
            //封装get请求
            HttpGet httpGet = new HttpGet(url);
            long startTime = System.currentTimeMillis();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)//设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(5000)//设置连接超时时间，单位毫秒
                    .setSocketTimeout(5000)//设置连接超时时间，单位毫秒
                    .build();
            httpGet.setConfig(requestConfig);
            //执行请求，获取response内容
            CloseableHttpResponse response = client.execute(httpGet);
            //获取页面实体对象
            HttpEntity entity = response.getEntity();
            //只能使用一次toString ，再次使用会发生stream closed的问题
            content = EntityUtils.toString(entity);
            // 403：IP被封，503：black，不知道为什么会返回这个信息
            if (response.getStatusLine().getStatusCode() == 403 || response.getStatusLine().getStatusCode() == 503) {
                logger.error("页面下载失败，url:{},代理信息：{}，返回内容:{}", url,proxyInfo,content);
                content = "";
            } else {
                logger.info("页面下载成功，消耗时间：{}，url:{}", System.currentTimeMillis() - startTime, url);
            }
        } catch (Exception e) {
            //可以在这里把失效的代理ip从本地代理库中删除掉，或者记住后，让后面的其他程序分析日志进行处理
            logger.error("页面下载失败，url:{},代理信息：{}", url, proxyInfo);
            content = "";
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 返回client对象
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        //获取httpclient对象（可以任务是获取到了一个浏览器对象）
        HttpClientBuilder builder = HttpClients.custom();
        if (Config.isProxy) {
            String ip_proxy = redisUtils.srandmember(RedisUtils.p_key);
            proxyInfo =ip_proxy;
            String[] prxoyArr = ip_proxy.split(":");
            HttpHost proxy = new HttpHost(prxoyArr[0], Integer.valueOf(prxoyArr[1]));
            logger.info("进行代理：{}", ip_proxy);
            return builder.setProxy(proxy).build();
        } else {
            return builder.build();
        }
    }
}
