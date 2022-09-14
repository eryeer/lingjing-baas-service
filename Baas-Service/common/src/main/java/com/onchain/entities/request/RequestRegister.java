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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestRegister {
    @ApiModelProperty(value = "手机号")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = CommonConst.PASSWORD_REGEX, message = "密码至少包含英文大小写及数字")
    private String password;

    @ApiModelProperty(value = "注册验证码")
    @NotBlank
    private String registerCode;

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
