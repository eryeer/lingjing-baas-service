package com.onchain.util;

import com.onchain.constants.CommonConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileUtil {

    public static final String FILE_PDF = "pdf";
    public static final String FILE_JPG = "jpg";
    public static final String FILE_JPEG = "jpeg";
    public static final String FILE_PNG = "png";
    public static final String FILE_SOL = "sol";
    public static final String FILE_JSON = "json";
    public static final String FILE_DOC = "doc";
    public static final String FILE_DOCX = "docx";
    public static final String FILE_TXT = "txt";

    public static final String FILE_GIF = "gif";
    public static final String FILE_BMP = "bmp";
    public static final String FILE_SVG = "svg";

    // 获取文件后缀名
    public static String getFileSuffix(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            log.error("File lack of suffix.");
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    public static boolean isPicType(String fileSuffix) {
        return StringUtils.equalsAnyIgnoreCase(fileSuffix, FILE_JPG, FILE_JPEG, FILE_PNG);
    }

    /**
     * 判断OSS服务文件上传时文件的contentType
     *
     * @param suffix 文件后缀
     * @return String
     */
    public static String getContentType(String suffix) {
        if (StringUtils.equalsIgnoreCase(suffix, "bmp")) {
            return "image/bmp";
        }
        if (StringUtils.equalsIgnoreCase(suffix, "gif")) {
            return "image/gif";
        }
        if (StringUtils.equalsAnyIgnoreCase(suffix, "jpeg", "jpg", "png", "jpz")) {
            return "image/jpeg";
        }
        if (StringUtils.equalsAnyIgnoreCase(suffix, "html", "htm", "hts")) {
            return "text/html";
        }
        if (StringUtils.equalsIgnoreCase(suffix, "txt")) {
            return "text/plain";
        }
        if (StringUtils.equalsIgnoreCase(suffix, "pdf")) {
            return "application/pdf";
        }
        if (StringUtils.equalsAnyIgnoreCase(suffix, "pptx", "ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (StringUtils.equalsAnyIgnoreCase(suffix, "docx", "doc")) {
            return "application/msword";
        }
        if (StringUtils.equalsIgnoreCase(suffix, "xml")) {
            return "text/xml";
        }
        if (StringUtils.equalsIgnoreCase(suffix, "zip")) {
            return "application/zip";
        }
        return "application/octet-stream";
    }

    public static boolean isFileTypeSupport(String fileSuffix, String fileType) {
        if (!StringUtils.equalsAny(fileType, CommonConst.FILE_IDA, CommonConst.FILE_IDB, CommonConst.FILE_BL, CommonConst.FILE_BLC,
                CommonConst.FILE_SOL, CommonConst.FILE_WALLET)) {
            return false;
        }

        // 目前仅支持 jpg,png,pdf,sol
        if (!StringUtils.equalsAnyIgnoreCase(fileSuffix, FILE_JPEG, FILE_JPG, FILE_PNG, FILE_PDF, FILE_SOL)) {
            return false;
        }

        // 身份证，营业执照必须为图片类型(或PDF)
        if (StringUtils.equalsAny(fileType, CommonConst.FILE_IDA, CommonConst.FILE_IDB, CommonConst.FILE_BL, CommonConst.FILE_BLC)) {
            return isPicType(fileSuffix);
        }

        // SOL格式校验
        if (StringUtils.equalsAny(fileType, CommonConst.FILE_SOL)) {
            return StringUtils.equalsAnyIgnoreCase(fileSuffix, FILE_SOL);
        }

        return true;
    }
}
