package com.onchain.service;


import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.exception.CommonException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class SmsServiceTest {
    private ParamsConfig paramsConfig;

    @Test(expected = CommonException.class)
    public void sendVerifyCode() throws CommonException {
        paramsConfig = new ParamsConfig();
        paramsConfig.smsSecretId = "";
        paramsConfig.smsSecretKey = "";
        paramsConfig.smsEndpoint = "";

        String phoneNumber = "1988";
        String smsTemplateId = "1988";
        String verifyCode = "123456";

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
            throw new CommonException(ReturnCode.SMS_SEND_ERROR);
        }
        if (!StringUtils.equals(resp.getSendStatusSet()[0].getCode(), CommonConst.SMS_OK)) {
            throw new CommonException(ReturnCode.SMS_SEND_ERROR);
        }
    }

    @Test
    public void test1() {
        Date a = DateUtils.ceiling(new Date(), Calendar.DATE);
        System.out.println(a);
    }
}