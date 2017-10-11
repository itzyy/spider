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
        try {
            if (writer == null) {
                writer = new FileWriter(Config.filePath, true);
                buff = new BufferedWriter(writer);
            }
            buff.append(str);
            buff.append("\n");
            buff.flush();
            //buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
