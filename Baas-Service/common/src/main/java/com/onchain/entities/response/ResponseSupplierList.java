package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
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
 * @date:2021-02-22 18:34
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseSupplierList {
    @ApiModelProperty(value = "供应商id/客户id")
    private String supplierId;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "认证申请时间")
    private Date applyTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "管理员名称")
    private String userName;
    @ApiModelProperty(value = "管理员手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "审批状态/认证状态")
    private String approveStatus;
    @ApiModelProperty(value = "审批时间/处理时间")
    private Date approveTime;

    @ApiModelProperty(value = "注册状态")
    private Boolean isRegistered;
    @ApiModelProperty(value = "运营状态")
    private String status;
}
