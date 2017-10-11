package com.spider.storeImpl;

import com.spider.domain.Page;
import com.spider.store.Storeable;
import com.spider.util.FileUtils;

/**
 * Created by Zouyy on 2017/10/11.
 */
public class FileStoreableImpl implements Storeable {

    public void store(Page page) {
        System.out.println("=========="+page.toString());
        FileUtils.writeToFile(page.toString());
    }
}
