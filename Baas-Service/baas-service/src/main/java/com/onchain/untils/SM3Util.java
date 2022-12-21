package com.onchain.untils;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.Security;

public class SM3Util {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] sm3(byte[] src) {
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(src, 0, src.length);
        byte[] hash = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    public static String sm3(String src) {
        byte[] bytes = src.getBytes(StandardCharsets.UTF_8);
        byte[] hash = sm3(bytes);
        return Hex.toHexString(hash);
    }
}
