package com.onchain.dna2explorer.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseContract {

    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "abi")
    private String abi;
    @ApiModelProperty(value = "合约创建时间")
    private Long blockTime;
    @ApiModelProperty(value = "合约部署者")
    private String creator;
    @ApiModelProperty(value = "合约部署交易")
    private String createTxHash;
    @ApiModelProperty(value = "合约类型")
    private String contractType;
    @ApiModelProperty(value = "token名称")
    private String tokenName;
    @ApiModelProperty(value = "token名称")
    private String tokenSymbol;
    @ApiModelProperty(value = "token小数位数")
    private Integer decimals;
}
