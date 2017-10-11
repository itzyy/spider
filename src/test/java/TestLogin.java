import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * 如果登陆需要验证图片
 *  1、需要图片识别算法
 *  2、可以去网站买服务
 *
 *  针对一些登陆流程复杂的网站
 *  htmlunit、webkit、selenim 缺陷：效率比较低(底层需要打开一个浏览器)，优点：会执行页面中的js
 *
 *  httpclient的缺点：不能执行js ; 执行效率高
 *
 */
public class TestLogin {

    @Test
    public void test1() throws IOException, URISyntaxException {
        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient client = builder.build();
        HttpPost httpPost = new HttpPost("http://svn.club/user/login");

        //配置参数信息，如uid对应页面的name=uid
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("uid","crxy"));
        parameters.add(new BasicNameValuePair("pwd","www.crxy.cn"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

        //装载页面元素
        httpPost.setEntity(entity);

        // 登陆
        CloseableHttpResponse response = client.execute(httpPost);

        //获取简单的响应代码
        int statusCode = response.getStatusLine().getStatusCode();
        // 302代表页面跳转
        if(statusCode==200){
            System.out.println("页面实现跳转");
            Header[] headers = response.getHeaders("Location");
            String location = headers[0].getValue();
            httpPost.setURI(new URI("http://svn.club"+location));
            //下载网页内容，进行装载
            CloseableHttpResponse execute = client.execute(httpPost);
            HttpEntity entity1 = execute.getEntity();
            System.out.println(EntityUtils.toString(entity1));

        }

    }
}
