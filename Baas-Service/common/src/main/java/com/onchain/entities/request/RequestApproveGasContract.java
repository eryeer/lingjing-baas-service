package com.onchain.entities.request;

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
public class RequestApproveGasContract {
    @ApiModelProperty(value = "签约的燃料数量")
    @NotBlank
    private String agreementAmount;

    @ApiModelProperty(value = "流水号")
    @NotBlank
    private String flowId;

    @ApiModelProperty(value = "核验反馈")
    private String feedback;

    @ApiModelProperty(value = "是否通过")
    @NotNull
    private Boolean isPass;
}
