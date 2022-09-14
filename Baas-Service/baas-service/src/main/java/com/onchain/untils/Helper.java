package com.onchain.untils;

import com.onchain.constants.CommonConst;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static final String SEPARATOR = "\\|\\|";


    /**
     * check param whether is null or ''
     *
     * @param params
     * @return boolean
     */
    public static Boolean isEmptyOrNull(Object... params) {
        if (params != null) {
            for (Object val : params) {
                if ("".equals(val) || val == null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static Boolean isNotEmptyOrNull(Object... params) {
        return !isEmptyOrNull(params);
    }

    /**
     * 判断时间范围是否超过一个月
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Boolean isTimeRangeExceedLimit(Long beginTime, Long endTime) {

        if ((endTime - beginTime) > (CommonConst.REQTIME_MAX_RANGE)) {
            return true;
        }
        return false;
    }


    /**
     * 判断时间范围是否超过一周
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Boolean isTimeRangeExceedWeek(Long beginTime, Long endTime) {

        if ((endTime - beginTime) > (CommonConst.REQTIME_MAX_RANGE_WEEK)) {
            return true;
        }
        return false;
    }


    /**
     * 获取真实请求ip
     *
     * @param request
     * @return
     */
    public static String getHttpReqRealIp(HttpServletRequest request) {

        String ip = "";
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }
        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

        if (ip.contains(":")) {
            int index = ip.indexOf(":");
            ip = ip.substring(0, index);
        }
        return ip;
    }

    public static String currentMethod() {
        return new Exception("").getStackTrace()[1].getMethodName();
    }


    /**
     * 根据base64后的图片数据生成本地文件
     *
     * @param imgStr
     * @param path
     * @return
     */
    public static File generateImage(String imgStr, String path) throws Exception {
        // 解密
        byte[] b = Base64.getDecoder().decode(imgStr);
        //处理数据
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        OutputStream out = new FileOutputStream(path);
        out.write(b);
        out.flush();
        out.close();
        File file = new File(path);
        return file;
    }

    /**
     * 获取区块时间当月的起始日期
     *
     * @param time
     * @return
     */
    public static Integer getMonthBeginTime(Integer time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time * 1000L));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int) (calendar.getTime().getTime() / 1000);
    }

    /**
     * 获取区块时间 + 一个月
     *
     * @param time
     * @return
     */
    public static int getNextMonthTime(int time) {
        Date nextMonth = DateUtils.addMonths(new Date(time * 1000L), 1);
        return (int) (nextMonth.getTime() / 1000L);
    }

    public static String getIPFromURL(String url) {
        Pattern pattern = Pattern.compile(CommonConst.IP_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }


}
