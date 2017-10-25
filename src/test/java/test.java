import com.spider.Spider;
import com.spider.domain.JdPage;
import com.spider.downloadImpl.HttpClientDownloadableImpl;
import com.spider.processImpl.JdProcessableImpl;
import com.spider.storeImpl.ConsoleStoreableImpl;
import com.spider.util.RedisUtils;
import com.spider.util.SleepUtils;
import org.junit.Test;

import java.util.Set;

public class test {

    @Test
    public void test1() {
        Spider spider = new Spider();
        spider.setDownloadable(new HttpClientDownloadableImpl());
        spider.setProcessable(new JdProcessableImpl());
        spider.setStoreable(new ConsoleStoreableImpl());
        JdPage jdPage = spider.download("http://list.jd.com/list.html?cat=9987,653,655");
        spider.process(jdPage);
//        spider.store(jdPage);
    }

    @Test
    public void test2() {
        RedisUtils redisUtils = new RedisUtils();
        Set<String> smember = redisUtils.smember(RedisUtils.b_key);
        String[] strings = smember.toArray(new String[smember.size()]);
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
    }

    @Test
    public void test3() {
        while(true){
            String[] s = new String[]{"182.34.102.237:9077",
                    "183.164.246.106:3852",
                    "36.40.202.108:1554",
                    "111.177.208.8:2341",
                    "182.139.112.41:2132",
                    "58.212.135.101:4813",
                    "117.57.23.77:6890",
                    "27.204.88.194:6544"
            };
            RedisUtils redisUtils = new RedisUtils();
            for (int i = 0; i < s.length; i++) {
                System.out.println(s[i]);
                redisUtils.add_s(RedisUtils.p_key,s[i]);
            }
            SleepUtils.sleep(5000);
        }

    }

}
