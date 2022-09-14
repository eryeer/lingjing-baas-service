package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTempContact {
    @ApiModelProperty(value = "合约abi")
    private String abi;
    @ApiModelProperty(value = "用户钱包文件uuid")
    private String walletFileUuid;
    @ApiModelProperty(value = "合约文件")
    private ResponseCosFile contractFile;
}
