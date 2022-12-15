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
public class ReponseChainAccountGasApplySummary {
    @ApiModelProperty(value = "账户名称")
    private String name;
    @ApiModelProperty(value = "账户地址")
    private String accountAddress;
    @ApiModelProperty(value = "剩余可用燃料总量")
    private String remainGas;
    @ApiModelProperty(value = "已申领的燃料总量")
    private String appliedGas;
    @ApiModelProperty(value = "最近申领时间")
    private Long recentlyApplyTime;
}
