package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestCredit {
    @ApiModelProperty(value = "核心企业编号")
    @NotBlank
    private String centerId;

    @ApiModelProperty(value = "授信合同号")
    @NotBlank
    private String contractNumber;

    @ApiModelProperty(value = "授信额度")
    @NotNull
    private Long creditAmount;

    @ApiModelProperty(value = "授信批复编号")
    @NotBlank
    private String approveNumber;

    @ApiModelProperty(value = "额度开始日")
    @NotNull
    private Date creditStartDate;

    @ApiModelProperty(value = "额度截止日")
    @NotNull
    private Date creditEndDate;

    @ApiModelProperty(value = "银行结算户")
    @NotBlank
    private String settlementAccount;

    @ApiModelProperty(value = "银行监管户")
    private String supervisedAccount;

    @ApiModelProperty(value = "授信附件uuid列表")
    @NotEmpty
    private List<String> creditFileUuidList;
}
