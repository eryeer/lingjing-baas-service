package com.onchain.controller;

import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.config.ParamsConfig;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestWechatSign;
import com.onchain.service.WechatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class WechatController {

    private final ParamsConfig paramsConfig;
    private final WechatService wechatService;

    @PostMapping(value = UrlConst.WECHAT_SIGNATURE)
    @ApiOperation(value = "获取微信公众号签名", notes = "获取微信公众号签名")
    @OperLogAnnotation(description = "wechatSignature")
    public ResponseFormat<?> wechatSignature(@Valid @RequestBody RequestWechatSign request,
                                             @RequestHeader(CommonConst.HEADER_API_TOKEN) String apiToken) {
        if (!StringUtils.equals(paramsConfig.apiToken, apiToken)) {
            return new ResponseFormat<>(ReturnCode.API_TOKEN_FAIL);
        }
        return new ResponseFormat<>(wechatService.getSignature(request.getUrl()));
    }
}
