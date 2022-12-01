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
public class ResponseChainAccountGasSummary {
    @ApiModelProperty(value = "链用户id")
    private Long Id;
    @ApiModelProperty(value = "链用户名称")
    private String accountName;
    @ApiModelProperty(value = "链用户地址")
    private String accountAddress;
    @ApiModelProperty(value = "已申领燃料量")
    private String applyAmount;
    @ApiModelProperty(value = "剩余燃料量")
    private String remain;
}
