package com.onchain.entities.request;

import com.onchain.constants.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date:2021-02-20 10:53
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestSupplier {

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;

    @NotBlank
    @ApiModelProperty(value = "管理员Id")
    private String userId;

    @NotBlank
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @NotBlank
    @ApiModelProperty(value = "统一社会信用代码")
    @Pattern(regexp = CommonConst.SOCIAL_CODE)
    private String uniSocialCreditCode;
    @NotBlank
    @ApiModelProperty(value = "注册地址")
    private String registerAddress;
    @NotBlank
    @ApiModelProperty(value = "经营地址")
    private String businessAddress;
    @NotBlank
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @NotBlank
    @ApiModelProperty(value = "银行账户")
    @Pattern(regexp = CommonConst.BANK_CODE)
    private String bankAccount;
    @NotBlank
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    @NotBlank
    @ApiModelProperty(value = "法人身份证")
    @Pattern(regexp = CommonConst.ID_NUMBER_REGEX)
    private String legalPersonIdn;
    @NotBlank
    @ApiModelProperty(value = "营业执照照片正面")
    private String businessLicenseFileUuid;
    @NotBlank
    @ApiModelProperty(value = "营业执照照片反面")
    private String businessLicenseCopyFileUuid;
    @NotBlank
    @ApiModelProperty(value = "法人身份证照片照片正面")
    private String legalPersonIdaFileUuid;
    @NotBlank
    @ApiModelProperty(value = "法人身份证照片照片反面")
    private String legalPersonIdbFileUuid;
    @NotBlank
    @ApiModelProperty(value = "基本开户许可证")
    private String openPermitFileUuid;
    @NotBlank
    @ApiModelProperty(value = "管理员姓名")
    private String userName;
    @NotBlank
    @ApiModelProperty(value = "管理员手机号")
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;
    @NotBlank
    @ApiModelProperty(value = "管理员邮箱")
    private String email;
    @NotBlank
    @ApiModelProperty(value = "管理员身份证号")
    @Pattern(regexp = CommonConst.ID_NUMBER_REGEX)
    private String idNumber;
    @NotBlank
    @ApiModelProperty(value = "管理员身份证正面")
    private String idaFileUuid;
    @NotBlank
    @ApiModelProperty(value = "管理员身份证反面")
    private String idbFileUuid;
}
