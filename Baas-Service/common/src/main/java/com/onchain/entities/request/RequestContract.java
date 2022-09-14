package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date:2021-03-15 14:42
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestContract {
    @ApiModelProperty(value = "Id")
    private Long id;
    @ApiModelProperty(value = "合同编号")
    @NotBlank
    private String contractNumber;
    @NotBlank
    @ApiModelProperty(value = "合同名称")
    private String contractName;


    @ApiModelProperty(value = "卖方名称")
    @NotBlank
    private String sellerName;
    @ApiModelProperty(value = "合同金额（单位为分）")
    @NotNull
    private Long amount;

    @ApiModelProperty(value = "合同文件")
    @NotBlank
    private String contractFileUuid;

}
