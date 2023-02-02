package com.onchain.service;

import com.onchain.constants.CommonConst;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class CosServiceTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private String testFilePath;
    private String testKey;
    private COSClient cosClient;
    // dev
//    String bucketName = "onchain-bill-dev-1302609605";
//    String secretId = "AKID1pNhmajVeX8UbWPaswjESWQ5Tt7AkusZ";
//    String secretKey = "2jFbhX8Weo8D8yc8RTEsGRRfKjsmOYR2";

    // test
    String bucketName = "onchain-bill-test-1302609605";
    String secretId = "AKIDczQ3ycrMTJsBKD6CKQXNwGg16uT1ovxj";
    String secretKey = "n1zg15IFuOcZFeJZZHm1zg50dz4vyvde";

    String PaymentCommitmentKey = CommonConst.PAYMENT_COMMIT_KEY;

    @BeforeEach
    public void setUp() {
        String regionName = "ap-shanghai";

        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);

        String path = "src/test/resources";

        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        testFilePath = absolutePath + "/cosTest.json";
        testKey = "example/obj";
    }

    @AfterEach
    public void tearDown() {
        cosClient.shutdown();
    }

    //    @Test
    public void upload() {
        String localFilePath = "D:\\tmp\\DNA2JavaScriptSDK-1.0.zip";
        File localFile = new File(localFilePath);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, CommonConst.DNA2_JS_SDK, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.info("Put object " + localFilePath + " as " + putObjectResult.getCrc64Ecma());
    }

    @Test
    public void getTempUrl() {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, CommonConst.DNA2_JS_SDK, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        log.info(url.toString());
    }
}