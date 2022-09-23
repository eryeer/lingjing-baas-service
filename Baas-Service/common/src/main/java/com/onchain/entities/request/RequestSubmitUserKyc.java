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
public class RequestSubmitUserKyc {
    @ApiModelProperty(value = "认证类型")
    @NotBlank
    private String userType;

    @ApiModelProperty(value = "企业名称")
    @NotBlank
    private String companyName;

    @ApiModelProperty(value = "统一社会信用代码")
    @NotBlank
    private String uniSocialCreditCode;

    @ApiModelProperty(value = "法人姓名")
    @NotBlank
    private String legalPersonName;

    @ApiModelProperty(value = "法人身份证号")
    @NotBlank
    private String legalPersonIdn;

    @ApiModelProperty(value = "用户姓名")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "用户身份证号")
    @NotBlank
    private String idNumber;

    @ApiModelProperty(value = "营业执照正本")
    @NotBlank
    private String businessLicenseFileUuid;

    @ApiModelProperty(value = "营业执照副本")
    @NotBlank
    private String businessLicenseCopyFileUuid;

    @ApiModelProperty(value = "用户身份证正面")
    @NotBlank
    private String idaFileUuid;

    @ApiModelProperty(value = "用户身份证反面")
    @NotBlank
    private String idbFileUuid;

    @ApiModelProperty(value = "法人身份证正面")
    @NotBlank
    private String legalPersonIdaFileUuid;

    @ApiModelProperty(value = "法人身份证正面")
    @NotBlank
    private String legalPersonIdbFileUuid;
}
