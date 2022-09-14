package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestUserPolicy {
    @ApiModelProperty(value = "协议名称")
    @NotBlank
    private String policyName;

    @ApiModelProperty(value = "协议trace id")
    @NotBlank
    private String traceId;

    @ApiModelProperty(value = "协议类型")
    @NotBlank
    private String policyType;

    @ApiModelProperty(value = "协议内容")
    @NotBlank
    private String policyContent;

    @ApiModelProperty(value = "协议版本号")
    @NotNull
    private Integer version;
}
