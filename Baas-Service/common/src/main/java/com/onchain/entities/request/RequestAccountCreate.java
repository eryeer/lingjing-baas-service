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
public class RequestAccountCreate {
    @ApiModelProperty(value = "链账户地址")
    @NotBlank
    private String chainAddress;

    @ApiModelProperty(value = "链账户名称")
    @NotBlank
    private String chainUserName;

    @ApiModelProperty(value = "签名信息")
    @NotBlank
    private String message;

    @ApiModelProperty(value = "经过签名处理的签名信息")
    @NotBlank
    private String signedMessage;
}
