package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.Contract;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestAppCreate;
import com.onchain.entities.request.RequestAppRemove;
import com.onchain.entities.request.RequestContractDeploy;
import com.onchain.entities.request.RequestContractUpdate;
import com.onchain.entities.response.*;
import com.onchain.exception.CommonException;
import com.onchain.service.ContractService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final JwtService jwtService;

//    @PostMapping(value = UrlConst.APP_CREATE)
    @ApiOperation(value = "创建合约应用", notes = "创建合约应用")
    @OperLogAnnotation(description = "appCreate")
    public ResponseFormat<?> appCreate(@Valid @RequestBody RequestAppCreate request,
                                       @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        contractService.appCreate(user.getUserId(), request.getAppName(), request.getTemplateType());
        return new ResponseFormat<>();
    }

//    @PostMapping(value = UrlConst.APP_REMOVE)
    @ApiOperation(value = "删除合约应用", notes = "删除合约应用")
    @OperLogAnnotation(description = "appRemove")
    public ResponseFormat<?> appRemove(@Valid @RequestBody RequestAppRemove request,
                                       @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        contractService.appRemove(user.getUserId(), request.getAppName());
        return new ResponseFormat<>();
    }

//    @GetMapping(value = UrlConst.GET_APP_LIST)
    @ApiOperation(value = "根据用户id获取应用列表", notes = "根据用户id获取应用列表")
    @OperLogAnnotation(description = "getAppList")
    public ResponseFormat<PageInfo<ResponseContractApp>> getAppList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                    @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        PageInfo<ResponseContractApp> result = contractService.getAppList(pageNumber, pageSize, user.getUserId());
        return new ResponseFormat<>(result);
    }

//    @GetMapping(value = UrlConst.GET_APP)
    @ApiOperation(value = "根据用户id和应用名称获取应用", notes = "根据用户id和应用名称获取应用")
    @OperLogAnnotation(description = "getApp")
    public ResponseFormat<ResponseContractApp> getApp(@RequestParam @NotBlank String appName,
                                                      @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws CommonException {
        User user = jwtService.parseToken(accessToken);
        ResponseContractApp result = contractService.getApp(user.getUserId(), appName);
        return new ResponseFormat<>(result);
    }

//    @GetMapping(value = UrlConst.GET_CONTRACT_TEMPLATES)
    @ApiOperation(value = "获取合约模板列表", notes = "获取合约模板列表")
    @OperLogAnnotation(description = "getContractTemplates")
    public ResponseFormat<List<ResponseContractTemplate>> getContractTemplates() throws CommonException {
        List<ResponseContractTemplate> result = contractService.getContractTemplates();
        return new ResponseFormat<>(result);
    }

//    @PostMapping(value = UrlConst.DEPLOY)
//    @ApiOperation(value = "合约部署", notes = "合约部署")
//    @OperLogAnnotation(description = "deploy")
//    public ResponseFormat<ResponseContractDepoly> deploy(@Valid @RequestBody RequestContractDeploy request,
//                                                         @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
//        User user = jwtService.parseToken(accessToken);
//        ResponseContractDepoly result = contractService.deploy(user.getUserId(), request);
//        return new ResponseFormat<>(result);
//    }

//    @PostMapping(value = UrlConst.UPDATE_FILE_LIST)
    @ApiOperation(value = "更新合约文件列表", notes = "更新合约文件列表")
    @OperLogAnnotation(description = "updateFileList")
    public ResponseFormat<?> updateFileList(@Valid @RequestBody RequestContractUpdate request,
                                            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
        User user = jwtService.parseToken(accessToken);
        contractService.updateFileList(user.getUserId(), request);
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_CONTRACT_BY_CHAIN_ACCOUNT_ADDRESS)
    @ApiOperation(value = "根据链账户地址查询合约", notes = "根据链账户地址查询合约")
    @OperLogAnnotation(description = "getContractByChainAccountAddress")
    public ResponseFormat<?> getContractByChainAccountAddress(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                              @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                              @RequestParam(required = false) String chainAccountAddress,
                                                              @RequestParam(required = false) String contractAddress,
                                                              @RequestParam(required = false) Long startTime,
                                                              @RequestParam(required = false) Long endTime,
                                            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws Exception {
        User user = jwtService.parseToken(accessToken);
        ResponseContractHolderInfo contracts = contractService.getContractByChainAccountAddress(pageNumber, pageSize, user.getUserId(), chainAccountAddress, contractAddress, startTime, endTime);
        return new ResponseFormat<>(contracts);
    }
}
