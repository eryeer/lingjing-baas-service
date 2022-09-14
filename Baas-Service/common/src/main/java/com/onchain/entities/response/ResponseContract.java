package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date: 2021-03-15 15:48
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseContract {
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "买方ID")
    private String buyerId;
    @ApiModelProperty(value = "卖方Id")
    private String sellerId;
    @ApiModelProperty(value = "买方名称")
    private String buyerName;
    @ApiModelProperty(value = "卖方名称")
    private String sellerName;
    @ApiModelProperty(value = "合同金额（单位为分）")
    private Long amount;
    @ApiModelProperty(value = "已关联金额")
    private Long linkedAmount;
    @ApiModelProperty(value = "是否关联金额")
    private Boolean linked;
    @ApiModelProperty(value = "合同文件")
    private String contractFileUuid;
    @ApiModelProperty(value = "合同文件")
    private ResponseCosFile contractFile;
    @ApiModelProperty(value = "运营状态")
    private String status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "最新修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "经办人")
    private String operatorName;
}
