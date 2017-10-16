package com.spider.processImpl;

import com.spider.domain.PoiPage;
import com.spider.process.Processable;
import com.spider.util.HtmlUtil;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * 解析poi数据
 */
public class PoiProcessableImpl implements Processable<PoiPage> {

    /**
     * 解析poi数据，
     *
     * @param page
     */
    public void process(PoiPage page) {
        //使用htmlcleaner解析对象
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        //对页面进行封装。转换成一个tagnode对象,通过xpath对页面元素可以进行快速标记
        TagNode rootNode = htmlCleaner.clean(page.getContext());
        try {
            //判断是否是第二页
            if (page.getUrl().startsWith("http://www.poi86.com/poi/province")) {
                Object[] province = rootNode.evaluateXPath("//ul[@class='list-group']//a");
                for (Object provinceObj : province) {
                    TagNode provinceNode = (TagNode) provinceObj;
                    String provinceStr = provinceNode.getAttributeByName("href");
                    page.setAdd("http://www.poi86.com" + provinceStr);
                }
            } else if (page.getUrl().startsWith("http://www.poi86.com/poi/district")) {//是否第三页
                Object[] district = rootNode.evaluateXPath("//table//a");
                //只选择名称href,不选择类型
                TagNode districtNode = (TagNode) district[0];
                String districtStr = districtNode.getAttributeByName("href");
                page.setAdd("http://www.poi86.com" + districtStr);

                //读取下一页地址
                Object[] nextPages = rootNode.evaluateXPath("//ul[@class='pagination']//a");
                if (nextPages != null && nextPages.length > 0) {
                    Object nextPage = nextPages[nextPages.length - 3];
                    TagNode node = (TagNode) nextPage;
                    String href = node.getAttributeByName("href");
                    String disabled = node.getParent().getAttributeByName("class");
                    if (disabled == null) {
                        page.setAdd("http://www.poi86.com" + href);
                    }
                }

            } else {
                parse(rootNode, page);
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析具体的poi数据
     *
     * @param rootNode
     * @param page
     */
    private void parse(TagNode rootNode, PoiPage page) throws XPatherException {
        //poi标题
        String head = HtmlUtil.getText(rootNode, "//h1");
        page.addField("head", head);
        //所属省份
        String province = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[1]/a");
        page.addField("province", province);
        //所属区县
        String distinct = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[2]/a");
        page.addField("distinct", distinct);
        //详细地址
        String address = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[3]").split(":")[1].trim();
        page.addField("address", address);
        //电话号码
        String phoneNo = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[4]").split(":")[1].trim();
        page.addField("phoneNo", phoneNo);
        //所属分类
        String type = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[5]").split(":")[1].trim();
        page.addField("type", type);
        //所属标签
        String tag = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[6]").split(":")[1].trim();
        page.addField("tag", tag);
        //大地坐标
        String eCoords = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[7]").split(":")[1].trim();
        page.addField("eCoords", eCoords);
        //火星坐标
        String mCoords = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[8]").split(":")[1].trim();
        page.addField("mCoords", mCoords);
        //百度坐标
        String bCoords = HtmlUtil.getText(rootNode, "//ul[@class='list-group']/li[8]").split(":")[1].trim();
        page.addField("bCoords", bCoords);

    }


}
