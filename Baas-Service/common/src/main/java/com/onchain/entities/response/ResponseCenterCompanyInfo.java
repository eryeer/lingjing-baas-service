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
 * @date:2021-02-23 18:31
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseCenterCompanyInfo {
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "统一社会信用代码")
    private String uniSocialCreditCode;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "银行账户")
    private String bankAccount;
    @ApiModelProperty(value = "注册地址")
    private String registerAddress;
    @ApiModelProperty(value = "经营地址")
    private String businessAddress;
    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;
    @ApiModelProperty(value = "法人身份证")
    private String legalPersonIdn;
    @ApiModelProperty(value = "营业执照照片正面")
    private String businessLicenseFileUuid;
    @ApiModelProperty(value = "营业执照照片反面")
    private String businessLicenseCopyFileUuid;
    @ApiModelProperty(value = "法人身份证照片照片正面")
    private String legalPersonIdaFileUuid;
    @ApiModelProperty(value = "法人身份证照片照片反面")
    private String legalPersonIdbFileUuid;
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
    @ApiModelProperty(value = "基本开户许可证")
    private String openPermitFileUuid;
    @ApiModelProperty(value = "核心企业id")
    private String centerId;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "注册状态")
    private String isRegistered;
    @ApiModelProperty(value = "运营状态")
    private String status;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "最新修改时间")
    private String updateTime;
    @ApiModelProperty(value = "营业执照正面")
    private ResponseCosFile businessLicenseFile;
    @ApiModelProperty(value = "营业执照照片反面")
    private ResponseCosFile businessLicenseCopyFile;
    @ApiModelProperty(value = "法人身份证照片照片正面")
    private ResponseCosFile legalPersonIdaFile;
    @ApiModelProperty(value = "法人身份证照片照片反面")
    private ResponseCosFile legalPersonIdbFile;
    @ApiModelProperty(value = "管理员身份证正面")
    private ResponseCosFile idaFile;
    @ApiModelProperty(value = "基本开户许可证")
    private ResponseCosFile openPermitFile;
    @ApiModelProperty(value = "管理员身份证反面")
    private ResponseCosFile idbFile;
    @ApiModelProperty(value = "uuid")
    private ResponseCosFile uuid;
}
