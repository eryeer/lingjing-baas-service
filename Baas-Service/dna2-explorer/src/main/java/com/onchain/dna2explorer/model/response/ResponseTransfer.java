package com.onchain.dna2explorer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTransfer {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "区块哈希")
    private String blockHash;
    @ApiModelProperty(value = "区块高度")
    private Long blockNumber;
    @ApiModelProperty(value = "发起地址")
    private String fromAddress;
    @ApiModelProperty(value = "接收地址")
    private String toAddress;
    @ApiModelProperty(value = "转账发出地址")
    private String transferFrom;
    @ApiModelProperty(value = "转账接收地址")
    private String transferTo;
    @ApiModelProperty(value = "token合约地址")
    private String contractAddress;
    @ApiModelProperty(value = "转账发出地址类型")
    private Integer fromType;
    @ApiModelProperty(value = "转账接收地址类型")
    private Integer toType;
    @ApiModelProperty(value = "tokeId")
    private String tokenId;
    @ApiModelProperty(value = "tokenName")
    private String tokenName;
    @ApiModelProperty(value = "tokenSymbol")
    private String tokenSymbol;
    @ApiModelProperty(value = "生成时间")
    private Long blockTime;
}
