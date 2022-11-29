package com.onchain.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.aop.operlog.OperLogAnnotation;
import com.onchain.constants.CommonConst;
import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.dao.GasContract;
import com.onchain.entities.dao.User;
import com.onchain.entities.request.RequestAppCreate;
import com.onchain.entities.request.RequestAppRemove;
import com.onchain.entities.request.RequestGasCreate;
import com.onchain.entities.response.ResponseGasContract;
import com.onchain.entities.response.ResponseUserGasSummary;
import com.onchain.service.ContractService;
import com.onchain.service.GasService;
import com.onchain.service.JwtService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


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
    public ResponseFormat<ResponseUserGasSummary> getGasSummary(@RequestHeader(CommonConst.HEADER_ACCESS_TOKEN) String accessToken) {
        User user = jwtService.parseToken(accessToken);
        ResponseUserGasSummary responseUserGasSummary = gasService.getGasContractSummary( user.getUserId());
        return new ResponseFormat<>(responseUserGasSummary);
    }

}
