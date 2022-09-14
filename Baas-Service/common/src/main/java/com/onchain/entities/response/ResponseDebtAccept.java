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
public class ResponseDebtAccept {
    @ApiModelProperty(value = "凭证编号")
    private String tokenId;
    @ApiModelProperty(value = "凭证金额")
    private Long amount;
    @ApiModelProperty(value = "资产到期日")
    private Date dueDate;
    @ApiModelProperty(value = "开立方id")
    private String issuerId;
    @ApiModelProperty(value = "开立方名称")
    private String issuerName;
    @ApiModelProperty(value = "开立日期")
    private Date issueDate;
    @ApiModelProperty(value = "凭证类型")
    private String tokenType;
    @ApiModelProperty(value = "资产转出方")
    private String fromSupplierName;
    @ApiModelProperty(value = "资产转让日")
    private Date transferDate;
    @ApiModelProperty(value = "签收处理日")
    private Date acceptDate;
    @ApiModelProperty(value = "签收意见")
    private String acceptMessage;
    @ApiModelProperty(value = "签收经办人")
    private String acceptOperatorName;
    @ApiModelProperty(value = "凭证状态")
    private String tokenStatus;
    @ApiModelProperty(value = "资产签发日")
    private Date sendDate;
}
