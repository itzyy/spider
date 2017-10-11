import com.spider.Spider;
import com.spider.domain.PoiPage;
import com.spider.download.Downloadable;
import com.spider.downloadImpl.PoiDownloadableImpl;
import com.spider.process.Processable;
import com.spider.processImpl.JdProcessableImpl;
import com.spider.processImpl.PoiProcessableImpl;
import com.spider.repositoyableImpl.RedisRepositorableImpl;
import com.spider.storeImpl.HbaseStoreableImpl;
import com.spider.util.PageUtils;
import org.junit.Test;

/**
 * Created by Zouyy on 2017/10/11.
 */
public class testPOI {

    @Test
    public  void test(){
        String url ="http://www.poi86.com/poi/11056753.html";
        //下载页面
        PageUtils.getContext(url);
        Downloadable<PoiPage> poiDownloadable = new PoiDownloadableImpl();
        Processable<PoiPage> poiProcessable = new PoiProcessableImpl();
        PoiPage page = poiDownloadable.download(url);
        poiProcessable.process(page);

    }

}
