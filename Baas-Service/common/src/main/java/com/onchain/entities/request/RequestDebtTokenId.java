package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author: PanChunYu
 * @program: SCF-Service
 * @JdkVersion: JDK1.8 语言版本
 * @date: 2021-03-25 16:34
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestDebtTokenId {
    @NotBlank
    @ApiModelProperty(value = "id")
    private String tokenId;
    @ApiModelProperty(value = "状态")
    private String tokenStatus;
}
