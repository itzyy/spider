package com.spider.downloadImpl;

import com.spider.download.Downloadable;
import com.spider.util.PageUtils;
import com.spider.domain.Page;

public class HttpClientDownloadableImpl implements Downloadable {

    /**
     * 下载功能
     * @param url   下载连接
     */
    public Page download(String url) {
        Page page = new Page();
        String context = PageUtils.getContext(url);
        page.setContent(context);
        page.setUrl(url);
        return page;
    }
}
