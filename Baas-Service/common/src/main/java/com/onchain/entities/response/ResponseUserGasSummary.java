package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserGasSummary {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "签约的燃料总量")
    private String totalAmount;
    @ApiModelProperty(value = "已申领燃料量")
    private String applyAmount;
    @ApiModelProperty(value = "未申领燃料量")
    private String unApplyAmount;
    @ApiModelProperty(value = "每个链账户的燃料分布")
    private List<ResponseChainAccountGasSummary> chainAccountGasDistribute;

}
