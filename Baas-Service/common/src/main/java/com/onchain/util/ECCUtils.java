package com.onchain.util;

import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class ECCUtils {


    public static final String KEY_ALGORITHM = "EC";

    public static final String KEY_STD_NAME = "secp256k1";//TODO复写生成密钥函数

    public static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS5Padding";

    protected static Logger logger = LoggerFactory.getLogger(ECCUtils.class);

    //此处由于string转为key形式没有好的转换函数，因此只能通过string拼接key的类结构，再转换为key。
    // 因此此处两个header其实是对应priKey和pubKey的java类结构
    public static final String PRIVATE_KEY_HEADER =
            "303E020100301006072A8648CE3D020106052B8104000A042730250201010420";

    public static final String PUBLIC_KEY_HEADER =
            "3056301006072A8648CE3D020106052B8104000A03420004";


    /*解密
     * in:  base64 String, base64 String,
     * out: String,
     */
    private static String decryptBySecret(String cipherData, String secret) throws Exception {

        // secret evolve from String to byte[]
        byte[] secretByte = decodeBase64(secret);
        byte[] cipherByte = decodeBase64(cipherData);

        SecretKeySpec skeySpec = new SecretKeySpec(secretByte, "AES");
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);

        //get iv and encrypted from cipherData
        byte[] ivbuf = silceBytes(cipherByte, 0, 16);
        byte[] encryptedbuf = silceBytes(cipherByte, 16, (cipherByte.length - 16));
        IvParameterSpec iv = new IvParameterSpec(ivbuf);

        //decrypt data
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(encryptedbuf);

        String originalString = new String(original, "utf-8");
        return originalString;
    }

    public static byte[] decodeBase64(String value) {
        Base64.Decoder base64Decoder = Base64.getDecoder();
        byte[] keyBytes = null;
        try {
            keyBytes = base64Decoder.decode(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyBytes;
    }

    public static String encodeBase64(byte[] bytes) {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(bytes);
    }

    private static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    private static byte[] silceBytes(byte[] data, int position, int length) {
        byte[] outData = new byte[length];
        System.arraycopy(data, position, outData, 0, length);
        return outData;
    }

    public static String SHA256(final String strText) {
        return SHA(strText);
    }

    private static String SHA(final String strText) {
        String strResult = null;

        if (strText != null && strText.length() > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(strText.getBytes(StandardCharsets.UTF_8));
                byte[] byteBuffer = messageDigest.digest();
                strResult = Hex.encodeHexString(byteBuffer);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    public static class SignObj {
        public String r;
        public String s;
        public byte v;
    }

}

@Data
class KeyConfig {
    public String priKey;
    public String pubKey;
}

