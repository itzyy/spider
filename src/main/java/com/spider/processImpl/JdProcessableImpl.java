package com.spider.processImpl;

import com.spider.process.Processable;
import com.spider.util.HtmlUtil;
import com.spider.util.PageUtils;
import com.spider.util.RevUtils;
import com.spider.domain.Page;
import org.apache.hadoop.yarn.webapp.view.Html;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xpath建议每个电商网站提起一套，保存在数据库，方便后期修改
 */
public class JdProcessableImpl implements Processable {

    Logger logger = LoggerFactory.getLogger(JdProcessableImpl.class);

    public void process(Page page) {
        //使用htmlcleaner解析对象
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        //对页面进行封装。转换成一个tagnode对象,通过xpath对页面元素可以进行快速标记
        TagNode rootNode = htmlCleaner.clean(page.getContent());
        //判断是否是列表还是商品明细界面
        if (page.getUrl().startsWith("http://list.jd.com/list.html")) {

            String href = HtmlUtil.getAttributeByName(rootNode, "//*[@id=\"J_topPage\"]/a[2]", "href");
            logger.info("===============http://list.jd.com" + href);
            if (!href.equals("javascript:;")) {
                page.addUrl("http://list.jd.com" + href);
            }
            //下一页地址
//            System.out.println("http://list.jd.com"+href);
            // 当前商品列表里面所有的url
            try {
                Object[] urlList = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
                //*[@id="J_topPage"]/a[2]
                for (Object obj : urlList) {
                    TagNode urlNode = (TagNode) obj;
                    page.addUrl("http:" + urlNode.getAttributeByName("href"));
//                    System.out.println("http:"+urlNode.getAttributeByName("href"));
                }
            } catch (XPatherException e) {
                e.printStackTrace();
            }

        } else {
            //解析商品明细界面
            parse(rootNode, page);
        }
    }

    private void parse(TagNode rootNode, Page page) {
        //根据xpath，进行快速查找
        // 两个//代表获取所有标签
        try {
            //获取标题
//            Object[] titleObjects = rootNode.evaluateXPath("//div[@class='sku-name']");
//            if (titleObjects != null && titleObjects.length > 0) {
//                //转换成tagnode对象
//                TagNode tagNode = (TagNode) titleObjects[0];
//                //获取标签里面的内容
//                System.out.println(tagNode.getText().toString());
//                page.addField("title",tagNode.getText().toString());
//            }
            String title = HtmlUtil.getText(rootNode, "//div[@class=\"sku-name\"]");
            page.addField("title", title);
            //获取图片
//            Object[] imageObjects = rootNode.evaluateXPath("//img[@id='spec-img']");
//            if (imageObjects != null && imageObjects.length > 0) {
//                TagNode imageNode = (TagNode) imageObjects[0];
//                String src = imageNode.getAttributeByName("data-origin");
//                System.out.println(src);
//                page.addField("image",src);
//            }
            String imgPath = HtmlUtil.getText(rootNode, "//img[@id='spec-img']");
            page.addField("imaPath", "http" + imgPath);
            //获取价格,由于价格是异步加载的，所以需要分析js请求
//            Object[] priceObjects = rootNode.evaluateXPath("//span[@class='price J-p-5089273']");
//            if (priceObjects != null && priceObjects.length > 0) {
//                TagNode priceNode = (TagNode) priceObjects[0];
//                String price = priceNode.getText().toString();
//           P

            //获取价格
            getPrice(page);
            //获取规格参数
            getGuiGe(page, rootNode);
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }

    private void getGuiGe(Page page, TagNode rootNode) throws XPatherException {
        Object[] itemObjs = rootNode.evaluateXPath("//*[@id=\"detail\"]/div[2]/div[2]/div[1]/div");
        //添加json，保存规格参数
        JSONArray specArray = new JSONArray();
        for (Object obj : itemObjs) {
            TagNode itemNode = (TagNode) obj;
            Object[] titles = itemNode.evaluateXPath("//h3");
            if (titles != null && titles.length > 0) {
                JSONObject h3Object = new JSONObject();
                TagNode titleNode = (TagNode) titles[0];
                h3Object.put(titleNode.getText().toString(), "");
                specArray.put(h3Object);
//                System.out.println("标题为："+titleNode.getText().toString());
            }
            Object[] dtNodes = itemNode.evaluateXPath("/dl/dt");
            Object[] ddNodes = itemNode.evaluateXPath("/dl/dd");

            int dd = 0;
            for (int i = 0; i < dtNodes.length; i++) {
                JSONObject dlJSON = new JSONObject();
                TagNode dtNode = (TagNode) dtNodes[i];
                TagNode ddNode = (TagNode) ddNodes[i + dd];
                if (ddNode.hasAttribute("class")) {
                    dd++;
                    ddNode = (TagNode) ddNodes[i + dd];
                }
                dlJSON.put(dtNode.getText().toString(), ddNode.getText().toString());
                specArray.put(dlJSON);
//                System.out.println(dtNode.getText().toString()+"========="+ddNode.getText().toString());
            }
        }
//        System.out.println(specArray.toString());
        page.addField("spec", specArray.toString());
    }

    private void getPrice(Page page) {
        String url = page.getUrl();
        Pattern pattern = Pattern.compile("http://item.jd.com/([0-9]+)+.html");
        Matcher matcher = pattern.matcher(url);
        String proId = "";
        if (matcher.find()) {
            //获取匹配到的正则表达式的内容
            proId = matcher.group(1);
            //设置编号
            page.setProductNo(RevUtils.reverse(proId) + "_jd");
            String context = PageUtils.getContext("http://p.3.cn/prices/mgets?skuIds=J_" + proId);
            JSONArray jsonArray = new JSONArray(context);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String price = jsonObject.getString("p");
            page.addField("price", price);
        }


//        System.out.println(page.getValues().get("price"));
    }
}
