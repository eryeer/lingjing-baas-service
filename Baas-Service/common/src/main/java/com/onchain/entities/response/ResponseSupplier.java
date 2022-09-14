package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date:2021-02-20 10:54
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseSupplier {
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "统一社会信用代码")
    private String uniSocialCreditCode;
    @ApiModelProperty(value = "注册地址")
    private String registerAddress;
    @ApiModelProperty(value = "经营地址")
    private String businessAddress;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "银行账户")
    private String bankAccount;
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    @ApiModelProperty(value = "法人身份证")
    private String legalPersonIdn;
    @ApiModelProperty(value = "审批状态")
    private String approveStatus;
    @ApiModelProperty(value = "审批反馈信息")
    private String approveFeedback;
    @ApiModelProperty(value = "审批时间")
    private String approveTime;
    @ApiModelProperty(value = "认证申请时间")
    private String applyTime;

    @ApiModelProperty(value = "营业执照照片正面")
    private String businessLicenseFileUuid;
    @ApiModelProperty(value = "营业执照照片反面")
    private String businessLicenseCopyFileUuid;
    @ApiModelProperty(value = "法人身份证照片照片正面")
    private String legalPersonIdaFileUuid;
    @ApiModelProperty(value = "法人身份证照片照片反面")
    private String legalPersonIdbFileUuid;
    @ApiModelProperty(value = "基本开户许可证")
    private String openPermitFileUuid;
    @ApiModelProperty(value = "管理员姓名")
    private String userName;
    @ApiModelProperty(value = "管理员手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "管理员邮箱")
    private String email;
    @ApiModelProperty(value = "管理员身份证号")
    private String idNumber;
    @ApiModelProperty(value = "管理员身份证正面")
    private String idaFileUuid;
    @ApiModelProperty(value = "管理员身份证反面")
    private String idbFileUuid;
}
