package com.onchain.dna2explorer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfig {
    @Value("${cloud.captcha.secretId}")
    public String secretId;

    @Value("${cloud.captcha.secretKey}")
    public String secretKey;

    @Value("${cloud.captcha.appSecretKey}")
    public String appSecretKey;

    @Value("${cloud.captcha.captchaAppId}")
    public Long captchaAppId;


    @Value("${cloud.captcha.userIp}")
    public String userIp;

    @Value("${cloud.captcha.endPoint}")
    public String endPoint;

}
