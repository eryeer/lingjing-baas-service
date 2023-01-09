package com.onchain.entities.response;

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
public class ResponseDashboardSummary {
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

    // current user
    @ApiModelProperty(value = "已部署合约数")
    private Integer deployCount;
    @ApiModelProperty(value = "链账户总数")
    private Integer accountCount;
    @ApiModelProperty(value = "已发送交易数")
    private Integer sendTxCount;

    @ApiModelProperty(value = "签约的燃料总量")
    private String agreementAmount;
    @ApiModelProperty(value = "已申领燃料量")
    private String applyAmount;
    @ApiModelProperty(value = "未申领燃料量")
    private String unApplyAmount;
    @ApiModelProperty(value = "可使用燃料量")
    private String balanceAmount;
}
