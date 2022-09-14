package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCrossChain {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "源链交易哈希")
    private String srcTxHash;
    @ApiModelProperty(value = "源链区块高度")
    private Long srcBlockNumber;
    @ApiModelProperty(value = "源链合约地址")
    private String srcContractAddress;
    @ApiModelProperty(value = "源链名称")
    private String srcChain;
    @ApiModelProperty(value = "源链合约名称")
    private String contractName;
    @ApiModelProperty(value = "跨链数据")
    private String data;
    @ApiModelProperty(value = "目标链合约地址")
    private String dstContractAddress;
    @ApiModelProperty(value = "目标链名称")
    private String dstChain;
    @ApiModelProperty(value = "目标链交易哈希")
    private String dstTxHash;
    @ApiModelProperty(value = "目标链区块高度")
    private Long dstBlockNumber;
    @ApiModelProperty(value = "跨链发起时间")
    private Date startTime;
    @ApiModelProperty(value = "跨链完成时间")
    private Date finishTime;
    @ApiModelProperty(value = "源链燃料消耗")
    private String gasUsed;
    @ApiModelProperty(value = "用户链账户地址")
    private String userAddress;
    @ApiModelProperty(value = "跨链状态")
    private String status;
}
