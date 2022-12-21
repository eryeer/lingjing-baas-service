package com.onchain.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.SjResponse;
import com.onchain.entities.TokenResponse;
import com.onchain.entities.request.RequestAssetRegister;
import com.onchain.untils.SM3Util;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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
        headers.set("Content-Type", "application/json");
        HttpEntity<JsonObject> headerEntity = new HttpEntity<>(headers);
        log.info(JSON.toJSONString(headers));
        SjResponse result = restTemplate.postForObject(paramsConfig.sjAssetRegisterUrl, request, SjResponse.class, headerEntity);
        return new ResponseFormat<>(result);
    }


}
