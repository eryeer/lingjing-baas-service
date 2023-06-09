package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestAcRequireGas;
import com.onchain.entities.request.RequestApproveGasContract;
import com.onchain.entities.request.RequestGasCreate;
import com.onchain.entities.response.*;
import com.onchain.service.GasService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.exceptions.TransactionException;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class GasController {

    private final GasService gasService;
    private final JwtService jwtService;

    @PostMapping(value = UrlConst.CREATE_GAS_CONTRACT)
    @ApiOperation(value = "创建燃料签约合同", notes = "创建燃料签约合同")
    @OperLogAnnotation(description = "createGasContract")
    public ResponseFormat<ResponseGasContract> createGasContract(@Valid @RequestBody RequestGasCreate request,
                                                                 @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        ResponseGasContract gasContract = gasService.createGasContract(user.getUserId(), request);
        return new ResponseFormat<>(gasContract);
    }

    @GetMapping(value = UrlConst.GET_GAS_CONTRACT_LIST)
    @ApiOperation(value = "获取燃料签约记录列表", notes = "获取燃料签约记录列表")
    @OperLogAnnotation(description = "getGasContractList")
    public ResponseFormat<PageInfo<ResponseGasContract>> getGasContractList(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("流水号") @RequestParam(required = false) String flowId,
            @ApiParam("审核状态") @RequestParam(required = false) Integer status,
            @ApiParam("上传日期的开始筛选时间") @RequestParam(required = false) Long uploadStartTime,
            @ApiParam("文件上传时间的终止筛选时间") @RequestParam(required = false) Long uploadEndTime,
            @ApiParam("审核完成的开始筛选时间") @RequestParam(required = false) Long approvedStartTime,
            @ApiParam("审核完成的终止筛选时间") @RequestParam(required = false) Long approvedEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        PageInfo<ResponseGasContract> gasContractList = gasService.getGasContractList(pageNumber, pageSize, user.getUserId(), flowId, uploadStartTime, uploadEndTime, status, approvedStartTime, approvedEndTime);
        return new ResponseFormat<>(gasContractList);
    }

    @GetMapping(value = UrlConst.GET_GAS_SUMMARY)
    @ApiOperation(value = "获取燃料统计", notes = "获取燃料统计")
    @OperLogAnnotation(description = "getGasSummary")
    public ResponseFormat<ResponseUserGasSummary> getGasSummary(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws IOException {
        User user = jwtService.parseToken(accessToken);
        ResponseUserGasSummary responseUserGasSummary = gasService.getGasContractSummary(user.getUserId());
        return new ResponseFormat<>(responseUserGasSummary);
    }

    @GetMapping(value = UrlConst.GET_ADMIN_GAS_CONTRACT_LIST)
    @ApiOperation(value = "获取PM视角燃料签约记录列表", notes = "获取PM视角燃料签约记录列表")
    @OperLogAnnotation(description = "getAdminGasContractList")
    public ResponseFormat<PageInfo<ResponseAdminGasContract>> getAdminGasContractList(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("用户手机号") @RequestParam(required = false) String phoneNumber,
            @ApiParam("企业名称") @RequestParam(required = false) String companyName,
            @ApiParam("签约数量") @RequestParam(required = false) String agreementAmount,
            @ApiParam("流水号") @RequestParam(required = false) String flowId,
            @ApiParam("审核状态") @RequestParam(required = false) Integer status,
            @ApiParam("是否审核中") @RequestParam(required = false) Boolean isApproving,
            @ApiParam("上传日期的开始筛选时间") @RequestParam(required = false) Long uploadStartTime,
            @ApiParam("文件上传时间的终止筛选时间") @RequestParam(required = false) Long uploadEndTime,
            @ApiParam("审核完成的开始筛选时间") @RequestParam(required = false) Long approvedStartTime,
            @ApiParam("审核完成的终止筛选时间") @RequestParam(required = false) Long approvedEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        PageInfo<ResponseAdminGasContract> gasContractList = gasService.getAdminGasContractList(pageNumber, pageSize, phoneNumber, companyName, agreementAmount, flowId, uploadStartTime, uploadEndTime, status, isApproving, approvedStartTime, approvedEndTime);
        return new ResponseFormat<>(gasContractList);
    }

    @PostMapping(value = UrlConst.APPROVE_GAS_CONTRACT)
    @ApiOperation(value = "PM审批燃料签约合同", notes = "PM审批燃料签约合同")
    @OperLogAnnotation(description = "approveGasContract")
    public ResponseFormat<?> approveGasContract(@Valid @RequestBody RequestApproveGasContract request,
                                                @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        gasService.approveGasContract(request);
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_GAS_CONTACT_STATISTIC_LIST)
    @ApiOperation(value = "PM获取燃料信息库中签约信息统计列表", notes = "PM获取燃料信息库中签约信息统计列表")
    @OperLogAnnotation(description = "getGasContactStatisticList")
    public ResponseFormat<PageInfo<ResponseGasContractStatistic>> getGasContactStatisticList(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("用户手机号") @RequestParam(required = false) String phoneNumber,
            @ApiParam("企业名称") @RequestParam(required = false) String companyName,
            @ApiParam("最近审批的开始筛选时间") @RequestParam(required = false) Long approvedStartTime,
            @ApiParam("最近审批的终止筛选时间") @RequestParam(required = false) Long approvedEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        PageInfo<ResponseGasContractStatistic> gasContractList = gasService.getGasContactStatisticList(pageNumber, pageSize, phoneNumber, companyName, approvedStartTime, approvedEndTime);
        return new ResponseFormat<>(gasContractList);
    }

    @PostMapping(value = UrlConst.ACQUIRE_GAS)
    @ApiOperation(value = "燃料申领", notes = "燃料申领")
    @OperLogAnnotation(description = "acquireGas")
    public ResponseFormat<?> acquireGas(
            @Valid @RequestBody RequestAcRequireGas requestAccGasRequire,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws InterruptedException, ExecutionException, TransactionException, IOException {
        User user = jwtService.parseToken(accessToken);
        gasService.acquireGas(user.getUserId(), requestAccGasRequire);
        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_CHAIN_ACCOUNT_LIST_FOR_GAS_MANAGEMENT)
    @ApiOperation(value = "获取燃料申领页的链账户列表", notes = "获取燃料申领页的链账户列表")
    @OperLogAnnotation(description = "getChainAccountListForGasManagement")
    public ResponseFormat<PageInfo<ResponseChainAccountGasClaimSummary>> getChainAccountListForGasManagement(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("用户id") @RequestParam(name = "userId") String userId,
            @ApiParam("链账号地址") @RequestParam(required = false) String userAddress,
            @ApiParam("链账号名称") @RequestParam(required = false) String name,
            @ApiParam("最近审批的开始筛选时间") @RequestParam(required = false) Long applyStartTime,
            @ApiParam("最近审批的终止筛选时间") @RequestParam(required = false) Long applyEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws IOException {
        User user = jwtService.parseToken(accessToken);
        if (!user.getUserId().equals(userId) && !user.getRole().equals(CommonConst.PM)){
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        PageInfo<ResponseChainAccountGasClaimSummary> chainAccountListForGasManagement = gasService.getChainAccountListForGasManagement(pageNumber, pageSize, userId, userAddress, name, applyStartTime, applyEndTime);
        return new ResponseFormat<>(chainAccountListForGasManagement);
    }

    @GetMapping(value = UrlConst.GET_GAS_CLAIM_HISTORY)
    @ApiOperation(value = "获取申领燃料记录", notes = "获取申领燃料记录")
    @OperLogAnnotation(description = "getGasClaimHistory")
    public ResponseFormat<PageInfo<ResponseGasClaimHistory>> getGasClaimHistory(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("链账号地址") @RequestParam(required = false) String userAddress,
            @ApiParam("链账号名称") @RequestParam(required = false) String name,
            @ApiParam("用户id") @RequestParam(required = false) String userId,
            @ApiParam("用户手机号") @RequestParam(required = false) String phoneNumber,
            @ApiParam("企业名称") @RequestParam(required = false) String companyName,
            @ApiParam("申领的开始筛选时间") @RequestParam(required = false) Long applyStartTime,
            @ApiParam("申领的终止筛选时间") @RequestParam(required = false) Long applyEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws IOException {
        User user = jwtService.parseToken(accessToken);
        if (!user.getRole().equals(CommonConst.PM) && !user.getUserId().equals(userId)) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        PageInfo<ResponseGasClaimHistory> list = gasService.getGasClaimHistory(pageNumber, pageSize, userId, userAddress, name, phoneNumber, companyName, applyStartTime, applyEndTime);
        return new ResponseFormat<>(list);
    }

    @GetMapping(value = UrlConst.GET_GAS_CLAIM_SUMMARY)
    @ApiOperation(value = "获取申领燃料统计列表", notes = "获取申领燃料统计列表")
    @OperLogAnnotation(description = "getGasClaimSummary")
    public ResponseFormat<PageInfo<ResponseUserGasClaimSummary>> getGasClaimSummary(
            @RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
            @ApiParam("用户手机号") @RequestParam(required = false) String phoneNumber,
            @ApiParam("企业名称") @RequestParam(required = false) String companyName,
            @ApiParam("最近申领的开始筛选时间") @RequestParam(required = false) Long applyStartTime,
            @ApiParam("最近申领的终止筛选时间") @RequestParam(required = false) Long applyEndTime,
            @RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) throws IOException {
        User user = jwtService.parseToken(accessToken);
        if (!StringUtils.equals(CommonConst.PM, user.getRole())) {
            return new ResponseFormat<>(ReturnCode.USER_ROLE_ERROR);
        }
        PageInfo<ResponseUserGasClaimSummary> list = gasService.getUserGasClaimSummary(pageNumber, pageSize, phoneNumber, companyName, applyStartTime, applyEndTime);
        return new ResponseFormat<>(list);
    }

}
