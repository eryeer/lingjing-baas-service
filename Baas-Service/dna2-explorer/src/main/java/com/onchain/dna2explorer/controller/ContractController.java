package com.onchain.dna2explorer.controller;

import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.constants.ReturnCode;
import com.onchain.dna2explorer.constants.UrlConst;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.request.RequestAbi;
import com.onchain.dna2explorer.model.response.ResponseAddress;
import com.onchain.dna2explorer.service.AddressService;
import com.onchain.dna2explorer.service.ContractService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

}
