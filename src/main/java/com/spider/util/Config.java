package com.spider.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/10/7.
 */
public class Config {
    static Properties properties ;
    /**
     * 使用时，只初始化一次
     */
    static {
        properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程变量
     */
    public static int nThread=Integer.valueOf(properties.getProperty("nThread"));
    public static int million_1=Integer.valueOf(properties.getProperty("million_1"));
    public static int million_5=Integer.valueOf(properties.getProperty("million_5"));
    /**
     * 文件变量
     */
    public static String filePath=properties.getProperty("filePath");
    /**
     * 代理获取地址
     */
    public static String proxyPath=properties.getProperty("proxyPath");
    /**
     * 是否启动代理
     */
    public static boolean isProxy= Boolean.parseBoolean(properties.getProperty("isProxy"));
}
