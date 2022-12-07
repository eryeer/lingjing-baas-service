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
public class ResponseGasContractStatistic {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "签约的燃料总量")
    private String totalAmount;
    @ApiModelProperty(value = "最近签约时间")
    private Long lastApprovedTime;
}
