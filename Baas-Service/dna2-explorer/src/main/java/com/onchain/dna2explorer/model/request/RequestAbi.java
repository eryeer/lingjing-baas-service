package com.onchain.dna2explorer.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestAbi {
    @ApiModelProperty(value = "合约地址", required = true)
    @NotBlank
    private String address;

    @ApiModelProperty(value = "abi", required = true)
    @NotBlank
    private String abi;

    @ApiModelProperty(value = "上传签名", required = true)
    @NotBlank
    private String signature;
}