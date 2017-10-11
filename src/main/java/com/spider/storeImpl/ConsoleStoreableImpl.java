package com.spider.storeImpl;

import com.spider.store.Storeable;
import com.spider.domain.Page;

/**
 * create 'spider','goodsinfo','spec'
 *goodsinfo使用版本号设置为30，建议为30
 * alter 'spider',{name=>'goodinfo',VERRSION=>30}
 * rowkey设置，使用商品编号，rowkey进行倒置，（防止出现rowkey倾斜），还要加上网站唯一标识
 * 例如：原始商品编号：5089273
 * 最终的编号为：3729805_jd
 */
public class ConsoleStoreableImpl implements Storeable {

    public void store(Page page) {
        System.out.println(page.getUrl()+"====="+page.getValues().get("price"));
    }
}
