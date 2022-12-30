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
public class RequestAssetDetail {
    @ApiModelProperty(value = "链id")
    @NotBlank
    private String thirdId;

    @ApiModelProperty(value = "对应区块链浏览器地址")
    @NotBlank
    private String webUrl;

    @ApiModelProperty(value = "合约地址")
    @NotBlank
    private String contractAddress;

    @ApiModelProperty(value = "token id")
    @NotBlank
    private String tokenId;

    @ApiModelProperty(value = "资产预览图地址")
    @NotBlank
    private String imageUrl;

    @ApiModelProperty(value = "销售价格 单位元")
    @NotNull
    private Double price;

    @ApiModelProperty(value = "销售时间 毫秒")
    @NotBlank
    private String buyTime;
}
