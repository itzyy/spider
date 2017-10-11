package com.spider;

import com.spider.domain.JdPage;
import com.spider.download.Downloadable;
import com.spider.downloadImpl.HttpClientDownloadableImpl;
import com.spider.process.Processable;
import com.spider.processImpl.JdProcessableImpl;
import com.spider.reposit.Repositoyable;
import com.spider.repositoyableImpl.QueueRepositorableImpl;
import com.spider.repositoyableImpl.RedisRepositorableImpl;
import com.spider.store.Storeable;
import com.spider.storeImpl.ConsoleStoreableImpl;
import com.spider.storeImpl.HbaseStoreableImpl;
import com.spider.util.Config;
import com.spider.util.SleepUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Spider{


    private Logger logger = LoggerFactory.getLogger(Spider.class);

    private Downloadable<JdPage> downloadable = new HttpClientDownloadableImpl();
    private Processable<JdPage> processable;
    private Storeable storeable = new ConsoleStoreableImpl();
    private Repositoyable repositoyable = new QueueRepositorableImpl();
    //创建固定线程池
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Config.nThread);

    /**
     * 启动爬虫
     */
    public void start() {
        check();
        logger.info("启动爬虫");
        while (true) {

            final String url = this.repositoyable.poll();
            if (StringUtils.isNotBlank(url)) {//判断是否为空
//                fixedThreadPool.execute(new Runnable() {
//                    public void run() {
//                        JdPage jdPage = Spider.this.download(url);
//                        Spider.this.process(jdPage);
//                        for (String nextUrl : jdPage.getUrls()) {
//                            Spider.this.repositoyable.add(nextUrl);
//                        }
//                        if (url.startsWith("http://item.jd.com")) {
//                            Spider.this.store(jdPage);
//                        }
//                        // 休息。防止爬虫ip被封
//                        SleepUtils.sleep(Config.million_1);
//                    }
//                });
                JdPage jdPage = Spider.this.download(url);
                Spider.this.process(jdPage);
                for (String nextUrl : jdPage.getUrls()) {
                    Spider.this.repositoyable.add(nextUrl);
                }
                if (url.startsWith("http://item.jd.com")) {
                    Spider.this.store(jdPage);
                }
                // 休息,防止爬虫ip被封
                SleepUtils.sleep(Config.million_1);
            } else {
                System.out.println("没有url!");
                SleepUtils.sleep(Config.million_5);
            }
        }
    }

    /**
     * 启动前，配置检查
     */
    private void check() {
        logger.info("开始执行配置检查");
        if (processable == null) {
            String message = "需要设置解析类";
            logger.error(message);
            throw new RuntimeException(message);
        }
        //打印实现类
        logger.info("========================配置检查开始=========================");
        logger.info("downloadable的实现类是:{}", downloadable.getClass().getName());
        logger.info("processable的实现类是:{}", processable.getClass().getName());
        logger.info("storeable的实现类是:{}", storeable.getClass().getName());
        logger.info("repositoyable的实现类是:{}", repositoyable.getClass().getName());
        logger.info("========================配置检查结束=========================");

    }

    /**
     * 下载
     */
    public JdPage download(String url) {
        return this.downloadable.download(url);

    }

    /**
     * 解析
     * 注意：
     * /html/body/div[5]/div/div[2]/div[1] 是无效的xpath标签
     * //div[@class='sku-name] 有效的xpath标签
     * // 代表所有标签
     * //div 代表所有标签下面的div
     * //div[@class='sku-name'] 代表所有标签下面的div class是sku-name
     */
    public void process(JdPage jdPage) {
        this.processable.process(jdPage);
    }

    /**
     * 入库存储
     */
    public void store(JdPage jdPage) {
        this.storeable.store(jdPage);
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
    }

    public void setStoreable(Storeable storeable) {
        this.storeable = storeable;
    }

    /**
     * 将队列queue添加url
     *
     * @param url
     */
    public void setSendUrl(String url) {
        this.repositoyable.add(url);
    }

    public void setRepositoyable(Repositoyable repositoyable) {
        this.repositoyable = repositoyable;
    }

    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.setProcessable(new JdProcessableImpl());
        spider.setStoreable(new HbaseStoreableImpl());
        spider.setRepositoyable(new RedisRepositorableImpl());
        // 这个地方需要进行修改，
        // 单独创建一个程序，负责向url仓库添加入口地址，定时添加，每天凌晨添加一次，可以实现每天循环抓取商品数据
        // 实现一个url调度器
        String url = "http://list.jd.com/list.html?cat=9987,653,655&page=168&sort=sort_rank_asc&trans=1&JL=6_0_0#J_main";
        spider.setSendUrl(url);
        //启动爬虫
        spider.start();
    }
}
