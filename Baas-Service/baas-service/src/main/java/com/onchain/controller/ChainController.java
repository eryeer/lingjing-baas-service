package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestAccountCreate;
import com.onchain.entities.request.RequestChangeGasTransfer;
import com.onchain.entities.request.RequestPrivateKeyCustody;
import com.onchain.entities.response.ResponseChainAccount;
import com.onchain.service.ChainService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @GetMapping(value = UrlConst.GET_CHAIN_ACCOUNT)
    @ApiOperation(value = "根据用户id获取链账户", notes = "根据用户id获取链账户")
    @OperLogAnnotation(description = "getChainAccount")
    public ResponseFormat<PageInfo<ResponseChainAccount>> getChainAccount(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                          @RequestParam String userId,
                                                                          @RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) String userAddress,
                                                                          @RequestParam(required = false) Boolean isGasTransfer,
                                                                          @RequestParam(required = false) Boolean isCustody,
                                                                          @RequestParam(required = false) Long startTime,
                                                                          @RequestParam(required = false) Long endTime,
                                                                          @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        PageInfo<ResponseChainAccount> result = chainService.getChainAccount(pageNumber, pageSize, user.getUserId(), name, userAddress, isGasTransfer, isCustody, startTime, endTime);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.CHANGE_GAS_TRANSFER_STATUS)
    @ApiOperation(value = "批量修改可转帐状态", notes = "批量修改可转帐状态")
    @OperLogAnnotation(description = "changeGasTransferStatus")
    public ResponseFormat<?> changeGasTransferStatus(@Valid @RequestBody RequestChangeGasTransfer request,
                                                     @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
        User user = jwtService.parseToken(accessToken);
        chainService.changeGasTransferStatus(user.getUserId(), request.getIds(), request.getIsGasTransfer());
        return new ResponseFormat<>();
    }

    @PostMapping(value = UrlConst.DELETE_CHAIN_ACCOUNT)
    @ApiOperation(value = "批量删除链账户", notes = "批量删除链账户")
    @OperLogAnnotation(description = "deleteChainAccount")
    public ResponseFormat<?> deleteChainAccount(@Valid @RequestBody List<Long> request,
                                                @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
        User user = jwtService.parseToken(accessToken);
        chainService.deleteChainAccount(user.getUserId(), request);
        return new ResponseFormat<>();
    }

}
