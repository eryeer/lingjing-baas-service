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
public class RequestGasCreate {
    @ApiModelProperty(value = "签约的燃料数量")
    @NotBlank
    private String agreementAmount;

    @ApiModelProperty(value = "合同文件")
    @NotBlank
    private String contractFileUUID;
}
