import com.spider.Spider;
import com.spider.downloadImpl.HttpClientDownloadableImpl;
import com.spider.processImpl.JdProcessableImpl;
import com.spider.storeImpl.ConsoleStoreableImpl;
import com.spider.domain.Page;
import org.junit.Test;

public class test {

    @Test
    public void test1(){
        Spider spider = new Spider();
        spider.setDownloadable( new HttpClientDownloadableImpl());
        spider.setProcessable(new JdProcessableImpl());
        spider.setStoreable(new ConsoleStoreableImpl());
        Page page = spider.download("http://list.jd.com/list.html?cat=9987,653,655");
        spider.process(page);
//        spider.store(page);
    }
}
