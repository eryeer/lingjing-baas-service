package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date: 2021-03-16 16:50
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestDebtToken {
    @ApiModelProperty(value = "凭证编号")
    private String tokenId;
    @ApiModelProperty(value = "开立金额")
    @NotNull
    private Long amount;
    @ApiModelProperty(value = "资产到期日")
    @NotNull
    private Date dueDate;

    @ApiModelProperty(value = "核心企业开立签名")
    private String issuerSignature;
    @ApiModelProperty(value = "合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty(value = "付款承诺函文件")
    private String paymentCommitmentFileUuid;
}
