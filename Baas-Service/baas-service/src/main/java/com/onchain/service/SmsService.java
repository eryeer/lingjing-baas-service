package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.dao.SmsCode;
import com.onchain.exception.CommonException;
import com.onchain.mapper.SmsCodeMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {
    private static final Integer LOGIN_CODE_VALID_SECONDS = 10 * 60; // 10 minutes
    private static final Integer AUTH_CODE_VALID_SECONDS = 14 * 24 * 60 * 60; // 14 days
    private static final Integer SEND_INTERVAL_SECONDS = 60; // 1 minute

    private final ParamsConfig paramsConfig;
    private final SmsCodeMapper smsCodeMapper;

    // 发送短信验证码
    private void sendVerifyCode(String phoneNumber, String verifyCode, String smsTemplateId) throws CommonException {
        Credential cred = new Credential(paramsConfig.smsSecretId, paramsConfig.smsSecretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(paramsConfig.smsEndpoint);

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        SmsClient client = new SmsClient(cred, "", clientProfile);

        SendSmsRequest req = new SendSmsRequest();
        // send to Chain mainland
        String[] phoneNumberSet1 = {CommonConst.CN_CODE + phoneNumber};
        req.setPhoneNumberSet(phoneNumberSet1);

        req.setSmsSdkAppid(paramsConfig.smsAppId);
        req.setTemplateID(smsTemplateId);
        req.setSign(paramsConfig.smsSign);

        String[] templateParamSet1 = {verifyCode};
        req.setTemplateParamSet(templateParamSet1);

        // success {"SendStatusSet":[{"SerialNo":"2019:1459782579269331713","PhoneNumber":"+8618801791237","Fee":1,"SessionContext":"","Code":"Ok","Message":"send success","IsoCode":"CN"}],"RequestId":"421bfbda-8906-4b36-9a20-f4b222d6c3d9"}
        // fail {"SendStatusSet":[{"SerialNo":"","PhoneNumber":"+8618801791237","Fee":0,"SessionContext":"","Code":"InvalidParameterValue.TemplateParameterFormatError","Message":"Verification code template parameter format error","IsoCode":""}],"RequestId":"a2a3ce6f-3a6b-4412-adcf-c98f6321ade1"}
        SendSmsResponse resp;
        try {
            resp = client.SendSms(req);
        } catch (TencentCloudSDKException ex) {
            log.error("SendVerifyCode error: " + ex.toString(), ex);
            throw new CommonException(ReturnCode.SMS_SEND_ERROR);
        }
        if (!StringUtils.equals(resp.getSendStatusSet()[0].getCode(), CommonConst.SMS_OK)) {
            log.error("SendVerifyCode error: " + JSON.toJSONString(resp));
            throw new CommonException(ReturnCode.SMS_SEND_ERROR);
        }
    }

    public String sendCode(String phoneNumber, String codeType, Integer seconds) throws CommonException {
        Date now = new Date();
        Date expire = DateUtils.addSeconds(now, seconds);
        SmsCode smsCode = smsCodeMapper.getLastSmsCode(phoneNumber, codeType);
        if (smsCode != null) {
            Date interval = DateUtils.addSeconds(smsCode.getSendTime(), SEND_INTERVAL_SECONDS);
            if (interval.after(now)) {
                throw new CommonException(ReturnCode.SMS_INTERVAL_ERROR);
            }
        }

        String code = RandomStringUtils.randomNumeric(6);
        String templateId = paramsConfig.verifyTempId;
        // 开发测试环境不发送
        if (!paramsConfig.smsTest) {
            sendVerifyCode(phoneNumber, code, templateId);
        }
        log.info("Skip sending code: " + paramsConfig.smsTest);
        SmsCode item = SmsCode.builder()
                .code(code)
                .codeType(codeType)
                .expirationTime(expire)
                .sendTime(now)
                .phoneNumber(phoneNumber)
                .build();
        smsCodeMapper.insertSmsCode(item);
        return code;
    }

    // 验证短信验证码
    public Boolean verifyCode(String phoneNumber, String codeType, String code) {
        SmsCode lastSmsCode = smsCodeMapper.getLastSmsCode(phoneNumber, codeType);
        if (lastSmsCode == null) {
            return false;
        }
        if (StringUtils.equals(code, lastSmsCode.getCode())) {
            // 使短信验证码失效
            smsCodeMapper.disableSmsCode(phoneNumber, codeType, code);
            return true;
        }
        return false;
    }
}
