package com.onchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class ParamsConfig {

    @Value("${cloud.cos.secretId}")
    public String cosSecretId;

    @Value("${cloud.cos.secretKey}")
    public String cosSecretKey;

    @Value("${cloud.cos.regionName}")
    public String cosRegionName;

    @Value("${cloud.cos.bucketName}")
    public String cosBucketName;

    @Value("${cloud.cos.httpProtocol}")
    public String cosHttpProtocol;

    @Value("${cloud.sms.secretId}")
    public String smsSecretId;

    @Value("${cloud.sms.secretKey}")
    public String smsSecretKey;

    @Value("${cloud.sms.endpoint}")
    public String smsEndpoint;

    @Value("${cloud.sms.appId}")
    public String smsAppId;

    @Value("${cloud.sms.verifyTempId}")
    public String verifyTempId;

    @Value("${cloud.sms.sign}")
    public String smsSign;

    @Value("${cloud.sms.test}")
    public Boolean smsTest;

    @Value("${dna.account.walletDir}")
    public String localWalletDir;

    @Value("${dna.account.adminAccount}")
    public String adminAccount;

    // PDFS config
    @Value("${pdfs.rpc.uploadUrl}")
    public String pdfsUploadUrl;
    @Value("${pdfs.rpc.downloadUrl}")
    public String pdfsDownloadUrl;
    @Value("${pdfs.cli.username}")
    public String pdfsUsername;
    @Value("${pdfs.cli.password}")
    public String pdfsPassword;
    @Value("${pdfs.cli.host}")
    public String pdfsHost;
    @Value("${pdfs.cli.port}")
    public Integer pdfsPort;
    @Value("${pdfs.cli.baseDir}")
    public String pdfsBaseDir;
    @Value("${pdfs.cli.uploadFolder}")
    public String pdfsUploadFolder;

    //privateKey encode
    @Value("${privatekey.encode.key}")
    public String privateEncodeKey;

    @Value("${privatekey.encode.offset}")
    public String privateEncodeOffset;

}
