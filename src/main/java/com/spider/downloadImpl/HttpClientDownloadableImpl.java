package com.spider.downloadImpl;

import com.spider.domain.JdPage;
import com.spider.download.Downloadable;
import com.spider.util.PageUtils;

public class HttpClientDownloadableImpl implements Downloadable<JdPage> {

    /**
     * 下载功能
     * @param url   下载连接
     */
    public JdPage download(String url) {
        JdPage jdPage = new JdPage();
        String context = PageUtils.getContext(url);
        jdPage.setContent(context);
        jdPage.setUrl(url);
        return jdPage;
    }
}
