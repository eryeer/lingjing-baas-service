package com.onchain.dna2explorer.controller;

import com.onchain.constants.UrlConst;
import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.response.ResponseSummary;
import com.onchain.dna2explorer.model.response.ResponseTotalSummary;
import com.onchain.dna2explorer.model.response.ResponseTxSummary;
import com.onchain.dna2explorer.service.SummaryService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping(value = UrlConst.GET_TOTAL_SUMMARY)
    @ApiOperation(value = "获取统计汇总信息", notes = "获取统计汇总信息")
    @OperLogAnnotation(description = "getTotalSummary")
    public ResponseFormat<ResponseTotalSummary> getTotalSummary() {
        ResponseTotalSummary result = summaryService.getTotalSummary();
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_ADDRESS_SUMMARY)
    @ApiOperation(value = "获取活跃地址统计列表", notes = "获取活跃地址统计列表")
    @OperLogAnnotation(description = "getAddressSummary")
    public ResponseFormat<List<ResponseSummary>> getAddressSummary(@RequestParam Integer limit) {
        List<ResponseSummary> result = summaryService.getAddressSummary(limit);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_BLOCK_SUMMARY)
    @ApiOperation(value = "获取每日新增区块数量统计列表", notes = "获取每日新增区块数量统计列表")
    @OperLogAnnotation(description = "getBlockSummary")
    public ResponseFormat<List<ResponseSummary>> getBlockSummary(@RequestParam Integer limit) {
        List<ResponseSummary> result = summaryService.getBlockSummary(limit);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_TRANSACTION_SUMMARY)
    @ApiOperation(value = "根据参数获取每日和每月交易数量统计列表", notes = "根据参数获取每日和每月交易数量统计列表")
    @OperLogAnnotation(description = "getTransactionSummary")
    public ResponseFormat<ResponseTxSummary> getTransactionSummary(@RequestParam Integer limit) {
        ResponseTxSummary result = summaryService.getTransactionSummary(limit);
        return new ResponseFormat<>(result);
    }

}
