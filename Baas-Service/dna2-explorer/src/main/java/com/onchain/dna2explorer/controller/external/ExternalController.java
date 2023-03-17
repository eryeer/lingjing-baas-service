package com.onchain.dna2explorer.controller.external;

import com.onchain.constants.UrlConst;
import com.onchain.dna2explorer.aop.operlog.OperLogAnnotation;
import com.onchain.dna2explorer.model.dao.ResponseFormat;
import com.onchain.dna2explorer.model.response.ResponseNFTHolder;
import com.onchain.dna2explorer.service.TokenService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ExternalController {

    private final TokenService tokenService;

    @GetMapping(value = UrlConst.EXTERNAL_GET_TOKEN_HOLDER)
    @ApiOperation(value = "对外接口 可以获取token信息持有信息", notes = "对外接口 可以获取token信息持有信息")
    @OperLogAnnotation(description = "getTokenHolder")
    public ResponseFormat<ResponseNFTHolder> getTokenHolder(@RequestParam(name = "pageNumber") @Min(1) Integer pageNumber,
                                                            @RequestParam(name = "pageSize") @Min(1) @Max(50) Integer pageSize,
                                                            @RequestParam String contractAddress) {
        ResponseNFTHolder result = tokenService.getNFTHolderListByContractAddress(pageNumber, pageSize, contractAddress);
        return new ResponseFormat<>(result);
    }

}
