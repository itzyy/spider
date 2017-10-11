package com.spider.storeImpl;

import com.spider.domain.Page;
import com.spider.store.Storeable;
import com.spider.util.HbaseUtils;
import com.spider.domain.JdPage;
import com.spider.util.RedisUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2017/10/6.
 */
public class HbaseStoreableImpl implements Storeable {

    HbaseUtils hbaseUtils = new HbaseUtils();
    RedisUtils redisUtils = new RedisUtils();

    /**
     * 将数据保存到hbase中
     *
     * @param page
     */
    public void store(Page page) {
        JdPage jdPage = (JdPage)page;
        String productNo = jdPage.getProductNo();
        try {
            hbaseUtils.put(HbaseUtils.TABLE_NAME, productNo, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_TITLE, jdPage.getValues().get("title"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, productNo, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PIC_URL, jdPage.getValues().get("imaPath"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, productNo, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PRICE, jdPage.getValues().get("price"));
            hbaseUtils.put(HbaseUtils.TABLE_NAME, productNo, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_DATA_URL, jdPage.getUrl());
            hbaseUtils.put(HbaseUtils.TABLE_NAME, productNo, HbaseUtils.COLUMNFAMILY_2, HbaseUtils.COLUMNFAMILY_2, jdPage.getValues().get("spec"));

            //存储数据的rowkey方面后期建议索引
            redisUtils.add("es_index", productNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
