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
public class RequestAccGasRequire {
    @ApiModelProperty(value = "申领的燃料数量")
    @NotBlank
    private String applyAmount;

    @ApiModelProperty(value = "申领地址")
    @NotBlank
    private String applyAccountAddress;
}
