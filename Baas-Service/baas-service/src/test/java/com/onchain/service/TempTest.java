package com.onchain.service;


import com.onchain.constants.ReturnCode;
import com.onchain.exception.CommonException;
import com.onchain.untils.Web3jUtil;
import com.onchain.util.ECCUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.test.FixedSecureRandom;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.onchain.constants.CommonConst.ADDRESS_HEADER;
import static com.onchain.constants.CommonConst.NONCE_HEADER;
import static com.onchain.util.CommonUtil.getLineFromStringByLineNum;


public class TempTest {



    @Test
    public void getTempUrl() throws Exception {
        String originalStr = "1f672139020a9d7846306659a9c025f562cb2d38d427781908e957f90c49979d";
        String skey = "aGVsbG9vbmNoYWluYnJvdA==";
        String offset = "1234567890123456";

        String encodeStr = ECCUtils.encryptBySecret(originalStr, skey, offset);
        System.out.println(encodeStr);
        String decodeStr = ECCUtils.decryptBySecret(encodeStr, skey, offset);
        System.out.println(decodeStr);
    }

    @Test
    public void sign() throws Exception {
        String prikey = "0x1f672139020a9d7846306659a9c025f562cb2d38d427781908e957f90c49979d";
        String originalStr = "金天太阳很大  hhhhhh 是不是";
//        byte[] contentHashBytes = Hash.sha3(originalStr.getBytes());
        byte[] contentHashBytes = originalStr.getBytes();
//        String contentHashHex = Hex.toHexString(Hash.sha3(originalStr.getBytes()));
//        System.out.println( new String(Hash.sha3(contentHashBytes))  );
        Credentials credentials = Credentials.create(prikey);
        Sign.SignatureData signMessage = Sign.signPrefixedMessage(contentHashBytes, credentials.getEcKeyPair());
        String signedstr = "0x" + Hex.toHexString(signMessage.getR()) + Hex.toHexString(signMessage.getS()) + Hex.toHexString(signMessage.getV());
        String address = Web3jUtil.getAddressFromETHPrivateKey(prikey);
        System.out.println(signedstr);
        System.out.println(address);
        System.out.println(Web3jUtil.isSignatureValid(signedstr, originalStr, address));
    }

    @Test
    public void sign2() throws Exception {
        String prikey = "0x1f672139020a9d7846306659a9c025f562cb2d38d427781908e957f90c49979d";
        String originalStr = "Welcome to LingJing BaaS Platform! Click to add account on LingJing BaaS Platform: www.lingjing-eco.com.cn This request will not trigger a blockchain transaction or cost any gas fees.Wallet address:0x1891a21e670EA305F8c8ECe2998Fb459d6f812aD Nonce:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] contentHashBytes = Hash.sha3(originalStr.getBytes());
        String contentHashHex = Hex.toHexString(Hash.sha3(originalStr.getBytes()));
        Credentials credentials = Credentials.create(prikey);
        Sign.SignatureData signMessage = Sign.signPrefixedMessage(contentHashBytes, credentials.getEcKeyPair());
        String signedstr = "0x" + Hex.toHexString(signMessage.getR()) + Hex.toHexString(signMessage.getS()) + Hex.toHexString(signMessage.getV());
        String address = Web3jUtil.getAddressFromETHPrivateKey(prikey);
        System.out.println(signedstr);
        System.out.println(address);
        System.out.println(originalStr );
        System.out.println(Web3jUtil.isSignatureValid(signedstr, originalStr, address));
    }

    @Test
    public void split(){
       byte[] bytes = new byte[]{100, 101, 102,  0 ,0, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0, 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0, 0 ,0 ,0 ,0};
        Bytes32 bytes32 = new Bytes32(bytes);
        for (byte b : bytes) {
            System.out.println(b);
        }
    }

//    @Test
//    public void transfer() throws ExecutionException, InterruptedException {
//        Web3j build = Web3j.build(new HttpService("https://maas-test-node.onchain.com"));
//        String from = Web3jUtil.getAddressFromETHPrivateKey("4b0c9b9d685db17ac9f295cb12f9d7d2369f5bf524b3ce52ce424031cafda1ae");
//        System.out.println(from);
//        Credentials credentials = Credentials.create("4b0c9b9d685db17ac9f295cb12f9d7d2369f5bf524b3ce52ce424031cafda1ae");
//        EthGetTransactionCount count = build.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
//        EthGetBalance ethGetBalance = build.ethGetBalance(from, DefaultBlockParameterName.LATEST).sendAsync().get();
//        System.out.println("count: " + ethGetBalance.getBalance());
//        System.out.println(new BigInteger("").toString());
//    }

}
