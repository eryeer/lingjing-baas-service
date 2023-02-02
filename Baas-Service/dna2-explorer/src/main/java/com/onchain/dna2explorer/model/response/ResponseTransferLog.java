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
public class ResponseTransferLog {
    @ApiModelProperty(value = "合约地址")
    private String address;
    @ApiModelProperty(value = "发送地址")
    private String fromAddress;
    @ApiModelProperty(value = "接收地址")
    private String toAddress;
    @ApiModelProperty(value = "转账数量")
    private String amount;
    @ApiModelProperty(value = "代币名称")
    private String tokenName;
    @ApiModelProperty(value = "代币标识")
    private String tokenSymbol;
    @ApiModelProperty(value = "小数位数")
    private Integer decimals;
    @ApiModelProperty(value = "ERC721 tokenId")
    private String tokenId;
    @ApiModelProperty(value = "ERC721/ERC20")
    private String contractType;
}
