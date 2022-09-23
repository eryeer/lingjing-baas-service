package com.onchain.entities.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    @ApiModelProperty(value = "用户状态")
    private String status;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "认证类型")
    private String userType;
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "手机")
    private String phoneNumber;
    @ApiModelProperty(value = "角色")
    private String role;
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "统一社会信用代码")
    private String uniSocialCreditCode;
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    @ApiModelProperty(value = "法人身份证号码")
    private String legalPersonIdn;
    @ApiModelProperty(value = "认证申请时间")
    private Date applyTime;
    @ApiModelProperty(value = "审批状态")
    private String approveStatus;
    @ApiModelProperty(value = "审批反馈信息")
    private String approveFeedback;
    @ApiModelProperty(value = "审批时间")
    private Date approveTime;

    @ApiModelProperty(value = "营业执照正本文件id")
    private String businessLicenseFileUuid;
    @ApiModelProperty(value = "法人身份证正面文件id")
    private String legalPersonIdaFileUuid;
    @ApiModelProperty(value = "法人身份证反面文件id")
    private String legalPersonIdbFileUuid;
    @ApiModelProperty(value = "身份证正面文件id")
    private String idaFileUuid;
    @ApiModelProperty(value = "身份证反面文件id")
    private String idbFileUuid;

    @ApiModelProperty(value = "营业执照正本")
    private ResponseCosFile businessLicenseFile;
    @ApiModelProperty(value = "法人身份证正面")
    private ResponseCosFile legalPersonIdaFile;
    @ApiModelProperty(value = "法人身份证反面")
    private ResponseCosFile legalPersonIdbFile;
    @ApiModelProperty(value = "身份证正面")
    private ResponseCosFile idaFile;
    @ApiModelProperty(value = "身份证反面")
    private ResponseCosFile idbFile;
}
