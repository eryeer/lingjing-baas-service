package com.onchain.dna2explorer.controller;

import com.github.pagehelper.PageInfo;
import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.constants.UrlConst;
import com.onchain.dna2explorer.model.dao.Contract;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.request.RequestAbi;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import com.onchain.dna2explorer.service.AddressService;
import com.onchain.dna2explorer.service.ContractService;
import com.onchain.entities.response.ResponseContractHolderInfo;
import com.onchain.entities.response.ResponseUserContractInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final AddressService addressService;

    @PostMapping(value = UrlConst.UPLOAD_ABI)
    @ApiOperation(value = "上传合约ABI文件", notes = "上传合约ABI文件")
    @OperLogAnnotation(description = "uploadAbi")
    public ResponseFormat<?> uploadAbi(@Valid @RequestBody RequestAbi request) {

        request.setAddress(request.getAddress().toLowerCase(Locale.ROOT));
        ResponseAddress address = addressService.getAddress(request.getAddress());
        if (address == null) {
            return new ResponseFormat<>(ReturnCode.CONTRACT_NOT_EXIST);
        }

        contractService.uploadAbi(address, request.getAbi(), request.getSignature());

        return new ResponseFormat<>();
    }

    @GetMapping(value = UrlConst.GET_CONTRACT_BY_CREATOR_ADDRESS)
    @ApiOperation(value = "根据链账户地址查看部署的合约", notes = "根据链账户地址查看部署的合约")
    @OperLogAnnotation(description = "getContractByCreatorAddress")
    public ResponseFormat<?> getContractByCreatorAddress(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                         @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                         @RequestParam List<String> userAddressList,
                                                         @RequestParam(required = false) String contractAddress,
                                                         @RequestParam(required = false) Long startTime,
                                                         @RequestParam(required = false) Long endTime) {

        if (userAddressList == null || userAddressList.size() == 0) {
            return new ResponseFormat<>(ReturnCode.CHAIN_ACCOUNT_NOT_EXIST);
        }

        ResponseContractHolderInfo contract = contractService.getContract(userAddressList, contractAddress, startTime, endTime, pageNumber, pageSize);

        return new ResponseFormat<>(contract);
    }

}
