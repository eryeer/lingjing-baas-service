package com.onchain.dna2explorer.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.aop.limit.RequestLimit;
import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.dna2explorer.constants.UrlConst;
import com.onchain.dna2explorer.exception.CommonException;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.request.RequestAddressList;
import com.onchain.dna2explorer.model.request.RequestDownloadTx;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import com.onchain.dna2explorer.model.response.ResponseTransaction;
import com.onchain.dna2explorer.model.response.ResponseTransfer;
import com.onchain.dna2explorer.model.response.ResponseTransferPageInfo;
import com.onchain.dna2explorer.service.AddressService;
import com.onchain.dna2explorer.service.CaptchaService;
import com.onchain.dna2explorer.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final CaptchaService captchaService;
    private final TransactionService transactionService;

    @GetMapping(value = UrlConst.GET_ADDRESS_LIST)
    @ApiOperation(value = "获取地址列表", notes = "获取地址列表")
    @OperLogAnnotation(description = "getAddressList")
    public ResponseFormat<PageInfo<ResponseAddress>> getAddressList(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize) {
        PageInfo<ResponseAddress> result = addressService.getAddressList(pageNumber, pageSize);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.GET_ADDRESS_LIST_BY_ADDRESS)
    @ApiOperation(value = "根据地址获取地址列表", notes = "根据地址获取地址列表")
    @OperLogAnnotation(description = "getAddressListByAddress")
    public ResponseFormat<List<ResponseAddress>> getAddressListByAddress(@Valid @RequestBody RequestAddressList request) {
        if (request.getAddressList().size() > 50) {
            return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, "query limit exceeded");
        }
        if (request.getAddressList().isEmpty()) {
            return new ResponseFormat<>(new ArrayList<>());
        }
        List<ResponseAddress> result = addressService.getAddressList(request.getAddressList());
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_ADDRESS)
    @ApiOperation(value = "获取地址详情", notes = "获取地址详情")
    @OperLogAnnotation(description = "getAddress")
    public ResponseFormat<ResponseAddress> getAddress(@RequestParam String address) {
        ResponseAddress result = addressService.getAddress(address);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_TRANSACTION_LIST_BY_ADDRESS)
    @ApiOperation(value = "根据地址获取交易列表", notes = "根据地址获取交易列表")
    @OperLogAnnotation(description = "getTransactionListByAddress")
    public ResponseFormat<PageInfo<ResponseTransaction>> getTransactionListByAddress(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                                     @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                                     @RequestParam String address) {
        PageInfo<ResponseTransaction> result = transactionService.getTransactionListByAddress(pageNumber, pageSize, address);
        return new ResponseFormat<>(result);
    }

    @GetMapping(value = UrlConst.GET_TRANSFER_LIST_BY_ADDRESS)
    @ApiOperation(value = "根据地址获取转账列表", notes = "根据地址获取转账列表")
    @OperLogAnnotation(description = "getTransferListByAddress")
    public ResponseFormat<ResponseTransferPageInfo> getTransferListByAddress(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                                               @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                                               @RequestParam String address) {
        ResponseTransferPageInfo result = addressService.getTransferListByAddress(pageNumber, pageSize, address);
        return new ResponseFormat<>(result);
    }

    @PostMapping(value = UrlConst.GET_TRANSFER_LIST_BY_ADDRESS_AND_TIME)
    @ApiOperation(value = "下载指定时间区间内某地址的前5000笔token transfer交易文件", notes = "下载指定时间区间内的某地址的前5000笔token transfer交易文件")
    @OperLogAnnotation(description = "downloadTransferList")
    @RequestLimit(limitNum = 1, name = "downloadTransferListLimit")
    public void downloadTransferList(@Valid @RequestBody RequestDownloadTx requestDownloadTx, HttpServletResponse response) throws CommonException {
        captchaService.sendCaptcha(requestDownloadTx.getTicket(), requestDownloadTx.getRandomStr());
        addressService.getLatest5kTransferListByAddress(requestDownloadTx.getAddress(), requestDownloadTx.getStartTime(), requestDownloadTx.getEndTime(), response);
    }


    @PostMapping(value = UrlConst.GET_TRANSACTION_LIST_BY_ADDRESS_AND_TIME)
    @ApiOperation(value = "下载指定时间区间内某地址的前5000笔交易文件", notes = "下载指定时间区间内某地址的前5000笔交易文件")
    @OperLogAnnotation(description = "downloadTransactionList")
    @RequestLimit(limitNum = 1, name = "downloadTransactionListLimit")
    public void downloadTransactionList(@Valid @RequestBody RequestDownloadTx requestDownloadTx, HttpServletResponse response) {
        captchaService.sendCaptcha(requestDownloadTx.getTicket(), requestDownloadTx.getRandomStr());
        transactionService.getLatest5kTransactionListByAddress(requestDownloadTx.getAddress(), requestDownloadTx.getStartTime(), requestDownloadTx.getEndTime(), response);
    }
}
