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
public class ResponseTotalSummary {

    @ApiModelProperty(value = "区块高度")
    private String blockNumber;
    @ApiModelProperty(value = "交易总数")
    private Long txCount;
    @ApiModelProperty(value = "节点总数")
    private Integer nodeCount;
    @ApiModelProperty(value = "活跃节点数")
    private Integer activeCount;
    @ApiModelProperty(value = "网络状态")
    private Boolean netStatus;
    @ApiModelProperty(value = "地址总数")
    private Integer addressCount;
}
