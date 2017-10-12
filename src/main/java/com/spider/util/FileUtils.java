package com.spider.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作工具类
 */
public class FileUtils {

    private static FileWriter writer;
    private static BufferedWriter buff;

    public static void writeToFile(String str) {
        try{
            FileWriter fileWriter =new FileWriter(Config.filePath,true);
            BufferedWriter buff =new BufferedWriter(fileWriter);
            buff.append(str);
            buff.append("\n");
            buff.flush();
            buff.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
