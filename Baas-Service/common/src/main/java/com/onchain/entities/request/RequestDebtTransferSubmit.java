package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestDebtTransferSubmit {
    @ApiModelProperty(value = "转让记录parentId")
    @Min(1)
    private Long parentId;

    @ApiModelProperty(value = "转让凭证图片uuid")
    @NotBlank
    private String transferTokenFileUuid;

    @ApiModelProperty(value = "自留凭证图片uuid")
    private String remainTokenFileUuid;
}
