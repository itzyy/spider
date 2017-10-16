package com.spider.util;

import com.spider.domain.JdPage;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Created by Administrator on 2017/10/6.
 */
public class HtmlUtil {

    /**
     * 解析结果
     */
    private static String result ="";

    /**
     * 根据父级标签，tagNode，通过xpath获取相关节点
     * @param tagNode
     * @param xpath
     * @return
     */
    public static String getText(TagNode tagNode,String xpath){
        init();
        try {
            Object[] nodes = tagNode.evaluateXPath(xpath);
            if(nodes!=null && nodes.length>0){
                TagNode node = (TagNode) nodes[0];
                result= node.getText().toString();
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据父级标签，tagNode，通过xpath的attribute获取指定属性的值
     * @param tagNode
     * @param xpath
     * @return
     */
    public static String getAttributeByName(TagNode tagNode,String xpath,String attName){
        init();
        try {
            Object[] nodes = tagNode.evaluateXPath(xpath);
            if(nodes!=null && nodes.length>0){
                TagNode node = (TagNode) nodes[0];
                result= node.getAttributeByName(attName);
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 初始化变量
     */
    private static void init(){
        result ="";
    }
}
