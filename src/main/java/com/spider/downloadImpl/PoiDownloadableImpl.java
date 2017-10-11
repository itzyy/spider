package com.spider.downloadImpl;

import com.spider.domain.PoiPage;
import com.spider.download.Downloadable;
import com.spider.util.PageUtils;

/**
 * poi 数据下载
 */
public class PoiDownloadableImpl implements Downloadable<PoiPage> {
    public PoiPage download(String url) {
        PoiPage poiPage = new PoiPage();
        String context = PageUtils.getContext(url);
        poiPage.setUrl(url);
        poiPage.setContext(context);
        return poiPage;
    }
}
