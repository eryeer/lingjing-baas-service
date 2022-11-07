package com.onchain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onchain.constants.CommonConst;
import com.onchain.entities.EventLog;
import com.onchain.entities.Notify;
import com.onchain.exception.ParameterException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by administrator on 17-9-4.
 */
public class CommonUtil {
    protected static Logger logger = LoggerFactory.getLogger(CommonUtil.class.getName());

    public final static String[] lowLetter = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z"
    };
    public final static String[] capLetter = {
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    public final static String[] speLetter = {
            "~", "!", "@", "#", "$", "%", "^",
            "&", "*", "+", "-", "_"
    };

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String getVersion() {
        InputStream is = CommonUtil.class.getClassLoader().getResourceAsStream("application.yml");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Properties properties = new Properties();
        try {
            properties.load(br);
            return properties.getProperty("version");
        } catch (IOException e) {
            logger.error("", e);
            return "";
        }
    }

    public static boolean checkPhoneNum(String phoneNum) {
        return Pattern.matches(CommonConst.PHONE_REGEX, phoneNum);
    }

    public static String getSHA256(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA3_256);
        md.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest();
        return Hex.encodeHexString(bytes);
    }

    public static String getIPFromURL(String url) {
        Pattern pattern = Pattern.compile(CommonConst.IP_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static <T> T json2Bean(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T t = mapper.readValue(json, clazz);
        return t;
    }

    public static String bean2Json(Object info) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(info);
        return jsonBody;
    }

    public static boolean strIsNullOrEmpty(String... para) {
        if (null == para) {
            return true;
        }

        for (int i = 0; i < para.length; i++) {
            if (null == para[i] || para[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static Date setBeginTime(Date beginTime) {
        if (null == beginTime) {
            return beginTime;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date setEndTime(Date endTime) {
        if (null == endTime) {
            return endTime;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getRandomOrderId() {
        // UUID.randomUUID().toString().replace("-","")
        Random random = new Random(System.currentTimeMillis());
        int value = random.nextInt();
        while (value < 0) {
            value = random.nextInt();
        }
        return value + "";
    }

    public static String getRandomPwd() {
        //获取8位数字作为随机密码
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());
        int seed = random.nextInt(5);
        for (int i = 0; i < 8; i++) {
            seed++;
            switch (seed % 5) {
                case 1:
                    stringBuffer.append(lowLetter[random.nextInt(lowLetter.length)]);
                    break;
                case 2:
                    stringBuffer.append(capLetter[random.nextInt(capLetter.length)]);
                    break;
                //目前不需要特殊字符
//                case 3 :
//                    stringBuffer.append(speLetter[random.nextInt(speLetter.length)]);
//                    break;
                default:
                    stringBuffer.append(seed % 10);
            }

        }
        return stringBuffer.toString();
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) throws ParameterException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new ParameterException("MD5加密出现错误");
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams, String key) throws ParameterException {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        System.out.println("md5:" + sb.toString());
        String sign = CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
        System.out.println("packge签名:" + sign);
        return sign;

    }

    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes(StandardCharsets.UTF_8);
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 获取从1970年开始到现在的秒数
     *
     * @param
     * @return
     */
    public static String getTimeStamp() {
        long seconds = System.currentTimeMillis() / 1000;
        return String.valueOf(seconds);
    }

    public static String decodeHexString(String hex) {
        byte[] bytes = DatatypeConverter.parseHexBinary(hex);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String encodeHexString(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return printHexBinary(bytes);
    }

    public static EventLog decodeEventLog(String eventStr) {
        EventLog eventLog = JSON.parseObject(eventStr, EventLog.class);
        if (eventLog != null && eventLog.getNotify().length > 0) {
            for (Notify notify : eventLog.getNotify()) {
                notify.setStates(decodeStates(notify.getStates()));
            }
        }
        return eventLog;
    }

    public static Object decodeStates(Object state) {
        if (state instanceof JSONArray) {
            JSONArray states = (JSONArray) state;
            List<Object> decodedStates = new ArrayList<>();
            for (Object item : states) {
                decodedStates.add(decodeStates(item));
            }
            return decodedStates;
        } else {
            return decodeHexString(state.toString());
        }
    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<>();
        for (T each : all) {
            if (!set.add(each)) {
                return true;
            }
        }
        return false;
    }

    public static String getLineFromStringByLineNum(String text, int number){
        StringReader stringReader = null;
        BufferedReader bufferedReader = null;
        String lineText = "";
        try {
            stringReader = new StringReader(text);
            bufferedReader = new BufferedReader(stringReader);
            List<String> collect = bufferedReader.lines().collect(Collectors.toList());
            lineText = collect.get(number);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (null != bufferedReader){
                    bufferedReader.close();
                }
                if (null != stringReader){
                    stringReader.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            return lineText;
        }
    }
}
