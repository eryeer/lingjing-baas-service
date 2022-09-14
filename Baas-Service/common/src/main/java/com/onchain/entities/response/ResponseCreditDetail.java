package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponseCreditDetail {
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "客户编号")
    private String centerId;
    @ApiModelProperty(value = "授信合同号")
    private String contractNumber;
    @ApiModelProperty(value = "授信额度")
    private Long creditAmount;
    @ApiModelProperty(value = "授信批复编号")
    private String approveNumber;
    @ApiModelProperty(value = "额度开始日")
    private Date creditStartDate;
    @ApiModelProperty(value = "额度截止日")
    private Date creditEndDate;
    @ApiModelProperty(value = "银行结算户")
    private String settlementAccount;
    @ApiModelProperty(value = "银行监管户")
    private String supervisedAccount;
    @ApiModelProperty(value = "授信附件Id列表")
    private String creditFileUuids;

    @ApiModelProperty(value = "授信附件列表")
    private List<ResponseCosFile> creditFiles;

}
