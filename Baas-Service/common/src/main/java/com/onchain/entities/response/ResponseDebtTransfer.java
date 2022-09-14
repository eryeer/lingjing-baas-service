package com.onchain.entities.response;

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
public class ResponseDebtTransfer {
    @ApiModelProperty(value = "父凭证id")
    private Long parentId;
    @ApiModelProperty(value = "父凭证编号")
    private String parentTokenId;
    @ApiModelProperty(value = "开立方id")
    private String issuerId;
    @ApiModelProperty(value = "开立方名称")
    private String issuerName;
    @ApiModelProperty(value = "接收方id")
    private String receiverId;
    @ApiModelProperty(value = "接收方名称")
    private String receiverName;
    @ApiModelProperty(value = "开立金额")
    private Long amount;
    @ApiModelProperty(value = "资产到期日")
    private Date dueDate;
    @ApiModelProperty(value = "签收状态")
    private String transferTokenStatus;
    @ApiModelProperty(value = "转让日")
    private Date transferDate;
    @ApiModelProperty(value = "转让方式")
    private String transferType;
    @ApiModelProperty(value = "转让金额")
    private Long transferAmount;
}
