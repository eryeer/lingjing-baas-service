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
public class ResponseGasClaimHistory {
    @ApiModelProperty(value = "申领状态")
    private Integer status;
    @ApiModelProperty(value = "账户名称")
    private String name;
    @ApiModelProperty(value = "账户地址")
    private String userAddress;
    @ApiModelProperty(value = "申领数量")
    private String applyAmount;
    @ApiModelProperty(value = "申领时间")
    private Long applyTime;
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
}
