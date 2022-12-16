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
public class ResponseUserGasClaimSummary {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "已申领的燃料总量")
    private String applyAmount;
    @ApiModelProperty(value = "最近申领时间")
    private Long applyTime;
}
