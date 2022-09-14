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
public class ResponseDebtTransferDetail {
    @ApiModelProperty(value = "转让日")
    private Date transferDate;
    @ApiModelProperty(value = "转让方式")
    private String transferType;
    @ApiModelProperty(value = "转让金额")
    private Long transferAmount;
    @ApiModelProperty(value = "接收方id")
    private String receiverId;
    @ApiModelProperty(value = "接收方名称")
    private String receiverName;
    @ApiModelProperty(value = "转让经办人")
    private String transferOperatorName;
    @ApiModelProperty(value = "签收状态")
    private String transferTokenStatus;
    @ApiModelProperty(value = "签收处理日")
    private Date acceptDate;
    @ApiModelProperty(value = "签收意见")
    private String acceptMessage;
    @ApiModelProperty(value = "转让关联合同id")
    private Long contractId;
    @ApiModelProperty(value = "已选债权凭证编号")
    private String parentTokenId;
    @ApiModelProperty(value = "转让债权凭证编号")
    private String transferTokenId;
    @ApiModelProperty(value = "自留债权凭证编号")
    private String remainTokenId;
    @ApiModelProperty(value = "转让关联合同")
    private ResponseContract contract;
    @ApiModelProperty(value = "已选债权凭证")
    private ResponseDebtToken parentToken;
    @ApiModelProperty(value = "转让债权凭证")
    private ResponseDebtToken transferToken;
    @ApiModelProperty(value = "自留债权凭证")
    private ResponseDebtToken remainToken;
}
