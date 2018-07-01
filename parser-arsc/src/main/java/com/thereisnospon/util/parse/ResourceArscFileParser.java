package com.thereisnospon.util.parse;

import com.thereisnospon.util.parse.type.ResFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * Created by yzr on 2018/6/20.
 */
public class ResourceArscFileParser {
    /**
     * 解析一个 resource.arsc 文件
     *
     * @param path 文件路径
     * @return
     */
    public static ResFile parseFile(String path) {
        try (FileInputStream fs = new FileInputStream(path)) {
            return parseFile(fs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析一个 resource.arsc 文件
     *
     * @param fis 输入流
     * @return
     */
    public static ResFile parseFile(InputStream fis) throws Exception {
        byte[] srcByte = null;
        ByteArrayOutputStream bos = null;
        bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        srcByte = bos.toByteArray();
        return ResFile.parseResFile(srcByte);
    }
}
