package com.spider.proxy;

import com.spider.util.Config;
import com.spider.util.RedisUtils;
import com.spider.util.SleepUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 将通过代理获取的API，注入到redis中
 */
public class ProxyManager {
    private static RedisUtils redisUtils = new RedisUtils();
    private static Logger logger = LoggerFactory.getLogger(ProxyManager.class);

    private static BufferedReader read = null;//读取访问结果

    public static void main(String[] args) {
        while(true){
            SleepUtils.sleep(7200000);
            sendProxy();
        }

    }

    public static void sendProxy() {
        try {
            URL realUrl = new URL(Config.proxyPath);
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            long startTimne = System.currentTimeMillis();
            //建立连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段，获取到cookies等
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            read = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;//循环读取
            while ((line = read.readLine()) != null) {
                if (checkProxyIp(line)) {
                    redisUtils.add_s(RedisUtils.p_key, line);
                }
            }
            logger.info("成功向redis中添加代理信息,耗时：{}", System.currentTimeMillis() - startTimne);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean checkProxyIp(String line) {
        try {
            String proxyIp = line.split(":")[0];
            int proxyPort = Integer.parseInt(line.split(":")[1]);
            HttpClientBuilder builder = HttpClients.custom();
            HttpHost proxy = new HttpHost(proxyIp, proxyPort);

            CloseableHttpClient client = builder.build();

            HttpGet httpGet = new HttpGet("http://www.poi86.com/poi/district/1116/1.html");
            httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
            httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(1000)//设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(1000)//设置连接超时时间，单位毫秒
                    .setSocketTimeout(1000)//设置连接超时时间，单位毫秒
                    .build();
            httpGet.setConfig(requestConfig);

            //执行请求，获取response内容
            CloseableHttpResponse response = client.execute(httpGet);
            //获取页面实体对象
            HttpEntity entity = response.getEntity();
            //只能使用一次toString ，再次使用会发生stream closed的问题
            String content = EntityUtils.toString(entity);
            int statueCode = response.getStatusLine().getStatusCode();

            if (statueCode == 200) {
                System.out.println(line);
                return true;
            }else
                return false;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }
        return false;
    }

}
