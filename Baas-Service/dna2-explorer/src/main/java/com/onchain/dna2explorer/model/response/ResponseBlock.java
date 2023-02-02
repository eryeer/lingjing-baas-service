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
public class ResponseBlock {

    @ApiModelProperty(value = "区块高度")
    private Long blockNumber;
    @ApiModelProperty(value = "区块哈希")
    private String blockHash;
    @ApiModelProperty(value = "区块生成时间")
    private Long blockTime;
    @ApiModelProperty(value = "记账地址")
    private String miner;
    @ApiModelProperty(value = "区块大小")
    private Integer blockSize;
    @ApiModelProperty(value = "消耗燃料")
    private Long gasUsed;
    @ApiModelProperty(value = "燃料上限")
    private Long gasLimit;
    @ApiModelProperty(value = "交易数量")
    private Integer txCount;
}
