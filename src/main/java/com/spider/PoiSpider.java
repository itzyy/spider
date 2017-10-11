package com.spider;

import com.spider.domain.PoiPage;
import com.spider.download.Downloadable;
import com.spider.downloadImpl.PoiDownloadableImpl;
import com.spider.process.Processable;
import com.spider.processImpl.PoiProcessableImpl;
import com.spider.reposit.Repositoyable;
import com.spider.repositoyableImpl.RedisRepositorableImpl;
import com.spider.store.Storeable;
import com.spider.storeImpl.FileStoreableImpl;
import com.spider.util.Config;
import com.spider.util.SleepUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PoiSpider {


    private Logger logger = LoggerFactory.getLogger(PoiSpider.class);
    private Logger poiLog = LoggerFactory.getLogger("poiLog");

    private Downloadable<PoiPage> downloadable;
    private Processable<PoiPage> processable;
    private Storeable storeable ;
    private Repositoyable repositoyable;
    //创建固定线程池
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Config.nThread);

    public static void main(String[] args) {
        PoiSpider spider = new PoiSpider();
        spider.setDownloadable(new PoiDownloadableImpl());
        spider.setProcessable(new PoiProcessableImpl());
        spider.setStoreable(new FileStoreableImpl());
        spider.setRepositoyable(new RedisRepositorableImpl());
        // 这个地方需要进行修改，
        // 单独创建一个程序，负责向url仓库添加入口地址，定时添加，每天凌晨添加一次，可以实现每天循环抓取商品数据
        // 实现一个url调度器
        String url = "http://www.poi86.com/poi/province/131.html";
        spider.setSendUrl(url);
        //启动爬虫
        spider.start();
    }

    /**
     * 启动爬虫
     */
    public void start() {
        check();
        logger.info("启动爬虫");
        while (true) {
            final String url = this.repositoyable.poll();
            if (StringUtils.isNotBlank(url)) {//判断是否为空
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        PoiPage poiPage = PoiSpider.this.download(url);
                        PoiSpider.this.process(poiPage);
                        for (String nextUrl : poiPage.getUrls()) {
                            PoiSpider.this.repositoyable.add(nextUrl);
                        }
                        Pattern pattern = Pattern.compile("http://www.poi86.com/poi/[0-9]+.html");
                        Matcher matcher = pattern.matcher(url);
                        if (matcher.find()) {
                              PoiSpider.this.store(poiPage);
                        }
                    }
                });


                // 休息。防止爬虫ip被封
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
    public PoiPage download(String url) {
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
    public void process(PoiPage poiPage) {
        this.processable.process(poiPage);
    }

    /**
     * 入库存储
     */
    public void store(PoiPage poiPage) {
        this.storeable.store(poiPage);
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


}
