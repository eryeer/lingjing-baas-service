package com.onchain.service;

import com.alibaba.fastjson.JSON;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.response.ResponseWeAccess;
import com.onchain.entities.response.ResponseWeSign;
import com.onchain.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class WechatService {

    private final RedisService redisService;
    private final RestTemplate restTemplate;
    private final ParamsConfig paramsConfig;

    // 获取微信公众号api ticket
    public String getApiTicket() throws CommonException {
        String ticket = redisService.getValue(CommonConst.WECHAT_TOKEN);
        if (StringUtils.isEmpty(ticket)) {
            String accessUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", paramsConfig.wechatAppId, paramsConfig.wechatAppSecret);
            log.info("access url:" + accessUrl);
            ResponseWeAccess result = restTemplate.getForObject(accessUrl, ResponseWeAccess.class);
            if (result == null || StringUtils.isEmpty(result.getAccess_token())) {
                log.error(JSON.toJSONString(result));
                throw new CommonException(ReturnCode.REQUEST_FAILED, JSON.toJSONString(result));
            }
            String accessToken = result.getAccess_token();
            String ticketUrl = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", accessToken);
            log.info("ticket url:" + ticketUrl);
            result = restTemplate.getForObject(ticketUrl, ResponseWeAccess.class);
            if (result == null || StringUtils.isEmpty(result.getTicket())) {
                log.error(JSON.toJSONString(result));
                throw new CommonException(ReturnCode.REQUEST_FAILED, JSON.toJSONString(result));
            }
            ticket = result.getTicket();
            redisService.setValueEX(CommonConst.WECHAT_TOKEN, ticket, result.getExpires_in());
        }
        return ticket;
    }

    // 获取微信公众号签名
    public ResponseWeSign getSignature(String url) throws CommonException {
        String ticket = getApiTicket();
        String timestamp = "" + System.currentTimeMillis() / 1000;
        String noncestr = RandomStringUtils.randomAlphabetic(10);
        ResponseWeSign result = ResponseWeSign.builder()
                .noncestr(noncestr)
                .timestamp(timestamp)
                .build();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("noncestr", noncestr);
        params.put("jsapi_ticket", ticket);
        params.put("timestamp", timestamp);
        params.put("url", url);
        StringBuilder signSrc = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            signSrc.append(entry.getKey()).append("=").append(entry.getValue());
            if (!StringUtils.equals(entry.getKey(), params.lastKey())) {
                signSrc.append("&");
            }
        }
        String signString = signSrc.toString();
        log.info("signString: " + signString);
        result.setSignature(DigestUtils.sha1Hex(signString));
        return result;
    }

}
