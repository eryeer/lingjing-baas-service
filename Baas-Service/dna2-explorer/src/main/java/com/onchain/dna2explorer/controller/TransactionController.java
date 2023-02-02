package com.onchain.dna2explorer.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.constants.UrlConst;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.response.ResponseTransaction;
import com.onchain.dna2explorer.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping(value = UrlConst.GET_TRANSACTION_LIST)
    @ApiOperation(value = "获取交易列表", notes = "获取交易列表")
    @OperLogAnnotation(description = "getTransactionList")
    public ResponseFormat<PageInfo<ResponseTransaction>> getTransactionList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                            @RequestParam(required = false) Long blockNumber) {
        PageInfo<ResponseTransaction> result = transactionService.getTransactionList(pageNumber, pageSize, blockNumber);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_TRANSACTION)
    @ApiOperation(value = "获取交易详情", notes = "获取交易详情")
    @OperLogAnnotation(description = "getTransaction")
    public ResponseFormat<ResponseTransaction> getTransaction(@RequestParam String txHash) {
        ResponseTransaction result = transactionService.getTransaction(txHash);
        return new ResponseFormat<>(result);
    }
}
