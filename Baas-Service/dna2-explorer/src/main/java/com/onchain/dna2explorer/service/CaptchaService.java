package com.onchain.dna2explorer.service;

import com.onchain.dna2explorer.config.CaptchaConfig;
import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.dna2explorer.exception.CommonException;
import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CaptchaService {

    private final CaptchaConfig captchaConfig;

    public void sendCaptcha(String ticket, String randomStr) throws CommonException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(captchaConfig.secretId, captchaConfig.secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(captchaConfig.endPoint);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        CaptchaClient client = new CaptchaClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeCaptchaResultRequest req = new DescribeCaptchaResultRequest();
        req.setCaptchaType(9L);
        req.setTicket(ticket);

        req.setUserIp(captchaConfig.userIp);
        req.setRandstr(randomStr);
        req.setCaptchaAppId(captchaConfig.captchaAppId);
        req.setAppSecretKey(captchaConfig.appSecretKey);
        // 返回的resp是一个DescribeCaptchaResultResponse的实例，与请求对象对应

        DescribeCaptchaResultResponse resp = null;
        try {
            resp = client.DescribeCaptchaResult(req);
        } catch (TencentCloudSDKException e) {
            log.error("sendCaptcha err: {}", e.getMessage());
            throw new CommonException(ReturnCode.CAPTCHA_SEND_ERROR);
        }
        if (resp == null || resp.getCaptchaCode() != 1) {
            log.error("sendCaptcha err: {}", resp.getCaptchaMsg());
            throw new CommonException(ReturnCode.CAPTCHA_SEND_ERROR);
        }
    }
}
