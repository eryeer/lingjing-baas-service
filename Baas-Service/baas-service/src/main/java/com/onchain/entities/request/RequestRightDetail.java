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
public class RequestRightDetail {
    @ApiModelProperty(value = "权益内容")
    @NotBlank
    private String rightInfo;

    @ApiModelProperty(value = "兑换流水号")
    @NotBlank
    private String rightNo;

    @ApiModelProperty(value = "兑换状态 未兑换、已兑换")
    @NotBlank
    private String exchangeStatus;

    @ApiModelProperty(value = "兑换时间 毫秒")
    @NotBlank
    private String exchangeTime;
}
