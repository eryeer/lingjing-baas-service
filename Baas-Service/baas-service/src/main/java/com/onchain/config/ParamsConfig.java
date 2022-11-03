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

    // maas chain config
    @Value("${chain.maas.adminAccount}")
    public String maasAdminAccount;

    @Value("${chain.maas.rpcUrl}")
    public String maasRpcUrl;

    @Value("${chain.maas.configAddress}")
    public String maasConfigAddress;

    //privateKey encode
    @Value("${privatekey.encode.key}")
    public String privateEncodeKey;

    @Value("${privatekey.encode.offset}")
    public String privateEncodeOffset;

}
