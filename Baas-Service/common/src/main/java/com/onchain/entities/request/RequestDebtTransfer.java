package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestDebtTransfer {
    @ApiModelProperty(value = "已选凭证转让记录id")
    private Long parentId;

    @ApiModelProperty(value = "已选凭证编号")
    @NotBlank
    private String parentTokenId;

    @ApiModelProperty(value = "合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty(value = "转让金额")
    @NotNull
    @Min(value = 100, message = "转让金额至少为1元")
    private Long transferAmount;
}
