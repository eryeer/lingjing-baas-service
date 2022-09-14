package com.onchain.entities.request;

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
public class RequestContractParameter {

    @ApiModelProperty(value = "参数类型，目前支持 uint256, bool, string, address")
    @NotBlank
    private String type;

    @ApiModelProperty(value = "参数内容")
    private String content;
}
