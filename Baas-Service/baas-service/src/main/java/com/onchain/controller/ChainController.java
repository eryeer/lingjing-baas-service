package com.onchain.controller;

import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.GasApply;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestAccountCreate;
import com.onchain.entities.request.RequestPrivateKeyCustody;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.exception.CommonException;
import com.onchain.service.ChainService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class ChainController {

    private final ChainService chainService;
    private final JwtService jwtService;

    @PostMapping(value = UrlConst.ACCOUNT_CREATE)
    @ApiOperation(value = "创建链账户", notes = "创建链账户")
    @OperLogAnnotation(description = "accountCreate")
    public ResponseFormat<ResponseChainAccount> accountCreate(@Valid @RequestBody RequestAccountCreate request,
                                                              @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        ResponseChainAccount result = chainService.accountCreate(user.getUserId(), request);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.PRIVATE_KEY_CUSTODY)
    @ApiOperation(value = "托管链账户私钥", notes = "托管链账户私钥")
    @OperLogAnnotation(description = "privateKeyCustody")
    public ResponseFormat<?> privateKeyCustody(@Valid @RequestBody RequestPrivateKeyCustody request,
                                                              @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        chainService.custodPrivateKey(request.getChainAccountId(), request.getPrivateKey());
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.DOWNLOAD_PRIVATE_KEY)
    @ApiOperation(value = "下载链账户私钥", notes = "下载链账户私钥")
    @OperLogAnnotation(description = "downloadPrivateKey")
    public ResponseFormat<ResponseChainAccount> downloadPrivateKey(@RequestParam Long accountId,
                                                                  @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        ResponseChainAccount result = chainService.getCustodPrivateKey(accountId);
        return new ResponseFormat<>(result);
    }

//    @GetMapping(value = UrlConst.GET_CHAIN_ACCOUNT)
//    @ApiOperation(value = "根据用户id获取链账户", notes = "根据用户id获取链账户")
//    @OperLogAnnotation(description = "getChainAccount")
//    public ResponseFormat<ResponseChainAccount> getChainAccount(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
//        User user = jwtService.parseToken(accessToken);
//        ResponseChainAccount result = chainService.getChainAccount(user.getUserId());
//        return new ResponseFormat<>(result);
//    }

//    @PostMapping(value = UrlConst.APPLY_GAS)
//    @ApiOperation(value = "申领燃料", notes = "申领燃料")
//    @OperLogAnnotation(description = "applyGas")
//    public ResponseFormat<?> applyGas(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
//        User user = jwtService.parseToken(accessToken);
//        String result = chainService.applyGas(user.getUserId());
//        return new ResponseFormat<>(result);
//    }

    @GetMapping(value = UrlConst.GET_APPLY_LIST)
    @ApiOperation(value = "根据用户id获取燃料申领记录", notes = "根据用户id获取燃料申领记录")
    @OperLogAnnotation(description = "getApplyList")
    public ResponseFormat<List<GasApply>> getApplyList(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        List<GasApply> result = chainService.getApplyList(user.getUserId());
        return new ResponseFormat<>(result);
    }
}
