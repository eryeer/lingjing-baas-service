package com.onchain.entities.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDebtToken {
    @ApiModelProperty(value = "凭证编号")
    private String tokenId;
    @ApiModelProperty(value = "父凭证编号")
    private String parentTokenId;
    @ApiModelProperty(value = "根凭证编号")
    private String rootTokenId;
    @ApiModelProperty(value = "开立金额")
    private Long amount;
    @ApiModelProperty(value = "资产到期日")
    private Date dueDate;
    @ApiModelProperty(value = "凭证状态")
    private String tokenStatus;
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商公钥")
    private String supplierPubKey;
    @ApiModelProperty(value = "开立方id")
    private String issuerId;
    @ApiModelProperty(value = "开立方名称")
    private String issuerName;
    @ApiModelProperty(value = "开立方核心企业公司")
    private String issuerPubKey;
    @ApiModelProperty(value = "开立日期")
    private Date issueDate;
    @ApiModelProperty(value = "开立经办人")
    private String issueOperatorName;
    @ApiModelProperty(value = "汇款方式")
    private String paymentType;
    @ApiModelProperty(value = "数字债权凭证图片")
    private String tokenFileUuid;
    @ApiModelProperty(value = "数字债权凭证图片")
    private ResponseCosFile tokenFile;
    @ApiModelProperty(value = "核心企业开立签名")
    private String issuerSignature;
    @ApiModelProperty(value = "关联合同Id")
    private Long contractId;
    @ApiModelProperty(value = "关联合同Id")
    private ResponseContract contract;
    @ApiModelProperty(value = "是否首次关联合同")
    private Boolean isFirstLinked;
    @ApiModelProperty(value = "凭证类型")
    private String tokenType;
    @ApiModelProperty(value = "付款承诺函文件")
    private String paymentCommitmentFileUuid;
    @ApiModelProperty(value = "付款承诺函文件")
    private ResponseCosFile paymentCommitmentFile;
    @ApiModelProperty(value = "资产转出方")
    private String fromSupplierName;
    @ApiModelProperty(value = "资产转让日")
    private Date transferDate;
    @ApiModelProperty(value = "转让方式")
    private String transferType;
    @ApiModelProperty(value = "转让金额")
    private Long transferAmount;
    @ApiModelProperty(value = "转让经办人")
    private String transferOperatorName;
    @ApiModelProperty(value = "签收处理日")
    private Date acceptDate;
    @ApiModelProperty(value = "签收意见")
    private String acceptMessage;
    @ApiModelProperty(value = "签收经办人")
    private String acceptOperatorName;
}
