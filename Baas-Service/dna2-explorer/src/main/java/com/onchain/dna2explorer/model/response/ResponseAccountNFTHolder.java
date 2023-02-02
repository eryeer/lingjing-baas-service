package com.onchain.dna2explorer.model.response;

import io.swagger.annotations.ApiModel;
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
@ApiModel
public class ResponseAccountNFTHolder {
    @ApiModelProperty(value = "id")
    private Long Id;
    @ApiModelProperty(value = "链户地址")
    private String accountAddress;
    @ApiModelProperty(value = "某个地址持有某个token的数量")
    private Long count;
    @ApiModelProperty(value = "持有的百分比")
    private Double percentage;
    @ApiModelProperty(value = "排名")
    private Long rank;
}
