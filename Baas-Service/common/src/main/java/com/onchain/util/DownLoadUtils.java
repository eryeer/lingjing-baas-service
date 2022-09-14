package com.onchain.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by Zhaochen on 3/22/18
 */
public class DownLoadUtils {
    public static String getAttachmentFileName(String filename,
                                               String browserType) throws UnsupportedEncodingException {
        if (browserType.contains("Firefox")) {
            // 访问火狐浏览器 =?utf-8?B?
            filename = "=?utf-8?B?"
                    + Base64.getEncoder().encode(filename.getBytes(StandardCharsets.UTF_8))
                    + "?=";
            // filename = MimeUtility.encodeText(filename, "utf-8", "B");

        } else {
            // IE CHROME 等浏览器 处理附件名乱码
            filename = URLEncoder.encode(filename, String.valueOf(StandardCharsets.UTF_8));
        }
        return filename;
    }

    public static String getTypePart(String fileName) {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1) {
            return "";
        } else {
            return fileName.substring(point + 1, length);
        }
    }

    public static String getFileType(File file) {
        return getTypePart(file.getName());
    }
}
