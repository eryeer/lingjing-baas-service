package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestAssetUpdate {
    @ApiModelProperty(value = "一级市场赠送数量")
    @NotNull
    private Long primaryMarketGiveCount;

    @ApiModelProperty(value = "一级市场销售数量")
    @NotNull
    private Long primaryMarketSellCount;

    @ApiModelProperty(value = "一级市场未售数量")
    @NotNull
    private Long primaryMarketStockCount;

    @ApiModelProperty(value = "链id")
    @NotBlank
    private String thirdId;

    @ApiModelProperty(value = "发行状态")
    @NotBlank
    private String issueStatus;

}
