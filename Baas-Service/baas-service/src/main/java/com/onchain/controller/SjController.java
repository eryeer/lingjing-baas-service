package com.onchain.controller;

import com.alibaba.fastjson.JSON;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.SjResponse;
import com.onchain.entities.TokenResponse;
import com.onchain.entities.request.RequestAssetCustomer;
import com.onchain.entities.request.RequestAssetRegister;
import com.onchain.entities.request.RequestAssetRights;
import com.onchain.entities.request.RequestAssetUpdate;
import com.onchain.untils.SM3Util;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class SjController {

    private final ParamsConfig paramsConfig;
    private final RestTemplate restTemplate;

    @GetMapping(value = UrlConst.GET_SJ_TOKEN)
    @ApiOperation(value = "获取数交所token", notes = "获取数交所token")
    @OperLogAnnotation(description = "getToken")
    public ResponseFormat<?> getToken() {
        String timestamp = "" + System.currentTimeMillis();
        String sigSrc = String.format("appId%sappSecret%stimestamp%s", paramsConfig.sjAppId, paramsConfig.sjAppSecret, timestamp);
        String sig = SM3Util.sm3(sigSrc);
        String getUrl = paramsConfig.sjGetTokenUrl + "?appId=" + paramsConfig.sjAppId + "&timestamp=" + timestamp + "&sign=" + sig;
        log.info(getUrl);
        SjResponse<TokenResponse> result = restTemplate.getForObject(getUrl, SjResponse.class);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.REFRESH_SJ_TOKEN)
    @ApiOperation(value = "刷新数交所token", notes = "刷新数交所token")
    @OperLogAnnotation(description = "refreshToken")
    public ResponseFormat<?> refreshToken(@RequestParam String refreshToken) {
        String timestamp = "" + System.currentTimeMillis();
        String sigSrc = String.format("appId%sappSecret%srefreshToken%stimestamp%s", paramsConfig.sjAppId, paramsConfig.sjAppSecret, refreshToken, timestamp);
        String sig = SM3Util.sm3(sigSrc);
        String getUrl = paramsConfig.sjRefreshTokenUrl + "?appId=" + paramsConfig.sjAppId + "&timestamp=" + timestamp + "&refreshToken=" + refreshToken + "&sign=" + sig;
        log.info(getUrl);
        SjResponse<TokenResponse> result = restTemplate.getForObject(getUrl, SjResponse.class);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.ASSET_REGISTER)
    @ApiOperation(value = "数交所资产注册", notes = "数交所资产注册")
    @OperLogAnnotation(description = "assetRegister")
    public ResponseFormat<?> assetRegister(@Valid @RequestBody @NotEmpty List<RequestAssetRegister> request,
                                           @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("appid", paramsConfig.sjAppId);
        headers.set("access-token", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info(JSON.toJSONString(headers));
        HttpEntity entity = new HttpEntity<>(request, headers);
        SjResponse result = restTemplate.postForObject(paramsConfig.sjAssetRegisterUrl, entity, SjResponse.class);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.ASSET_UPDATE)
    @ApiOperation(value = "数交所资产更新", notes = "数交所资产更新")
    @OperLogAnnotation(description = "assetUpdate")
    public ResponseFormat<?> assetUpdate(@Valid @RequestBody RequestAssetUpdate request) {
        String timestamp = "" + System.currentTimeMillis();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("apiTime", timestamp);
        params.put("appId", paramsConfig.sj2AppId);
        params.put("thirdId", request.getThirdId());
        params.put("primaryMarketGiveCount", request.getPrimaryMarketGiveCount().toString());
        params.put("primaryMarketSellCount", request.getPrimaryMarketSellCount().toString());
        params.put("primaryMarketStockCount", request.getPrimaryMarketStockCount().toString());
        params.put("issueStatus", request.getIssueStatus());
        StringBuilder signSrc = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            signSrc.append(entry.getKey()).append(entry.getValue());
        }
        signSrc.append("appkey").append(paramsConfig.sj2AppKey);
        String sign = DigestUtils.md5Hex(signSrc.toString());
        String url = paramsConfig.sj2AssetUpdateUrl + "?appId=" + paramsConfig.sj2AppId + "&apiTime=" + timestamp + "&sign=" + sign;
        log.info(signSrc.toString(), url);
        SjResponse result = restTemplate.postForObject(url, request, SjResponse.class);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.ASSET_CUSTOMER)
    @ApiOperation(value = "数交所资产客户关联", notes = "数交所资产客户关联")
    @OperLogAnnotation(description = "assetCustomer")
    public ResponseFormat<?> assetCustomer(@Valid @RequestBody RequestAssetCustomer request) {
        String timestamp = "" + System.currentTimeMillis();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("apiTime", timestamp);
        params.put("appId", paramsConfig.sj2AppId);
        params.put("customerId", request.getCustomerId());
        params.put("customerName", request.getCustomerName());
        params.put("ownerAddress", request.getOwnerAddress());
        StringBuilder signSrc = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            signSrc.append(entry.getKey()).append(entry.getValue());
        }
        signSrc.append("appkey").append(paramsConfig.sj2AppKey);
        String sign = DigestUtils.md5Hex(signSrc.toString());
        String url = paramsConfig.sj2AssetCustomerUrl + "?appId=" + paramsConfig.sj2AppId + "&apiTime=" + timestamp + "&sign=" + sign;
        log.info(signSrc.toString(), url);
        SjResponse result = restTemplate.postForObject(url, request, SjResponse.class);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.ASSET_RIGHTS)
    @ApiOperation(value = "数交所资产权益兑换", notes = "数交所资产权益兑换")
    @OperLogAnnotation(description = "assetRights")
    public ResponseFormat<?> assetRights(@Valid @RequestBody RequestAssetRights request) {
        String timestamp = "" + System.currentTimeMillis();
        TreeMap<String, String> params = new TreeMap<>();
        params.put("apiTime", timestamp);
        params.put("appId", paramsConfig.sj2AppId);
        params.put("customerId", request.getCustomerId());
        params.put("customerName", request.getCustomerName());
        params.put("exchangeStatus", request.getExchangeStatus());
        params.put("thirdId", request.getThirdId());
        params.put("tokenId", request.getTokenId());
        StringBuilder signSrc = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            signSrc.append(entry.getKey()).append(entry.getValue());
        }
        signSrc.append("appkey").append(paramsConfig.sj2AppKey);
        String sign = DigestUtils.md5Hex(signSrc.toString());
        String url = paramsConfig.sj2AssetRightsUrl + "?appId=" + paramsConfig.sj2AppId + "&apiTime=" + timestamp + "&sign=" + sign;
        log.info(signSrc.toString(), url);
        SjResponse result = restTemplate.postForObject(url, request, SjResponse.class);
        return new ResponseFormat<>(result);
    }
}
