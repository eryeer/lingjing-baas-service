package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestDebtAccept {
    @ApiModelProperty(value = "待签收凭证编号")
    @NotBlank
    private String tokenId;

    @ApiModelProperty(value = "签收意见")
    private String acceptMessage;
}
