import com.spider.Spider;
import com.spider.domain.JdPage;
import com.spider.downloadImpl.HttpClientDownloadableImpl;
import com.spider.processImpl.JdProcessableImpl;
import com.spider.storeImpl.ConsoleStoreableImpl;
import org.junit.Test;

public class test {

    @Test
    public void test1(){
        Spider spider = new Spider();
        spider.setDownloadable( new HttpClientDownloadableImpl());
        spider.setProcessable(new JdProcessableImpl());
        spider.setStoreable(new ConsoleStoreableImpl());
        JdPage jdPage = spider.download("http://list.jd.com/list.html?cat=9987,653,655");
        spider.process(jdPage);
//        spider.store(jdPage);
    }
}
