package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestAssetRights {
    @ApiModelProperty(value = "客户唯一 id")
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "客户名称或昵称")
    @NotBlank
    private String customerName;

    @ApiModelProperty(value = "兑换状态: 未兑换、部分兑换、全部兑换")
    @NotBlank
    private String exchangeStatus;

    @ApiModelProperty(value = "发行平台资产唯一 id")
    @NotBlank
    private String thirdId;

    @ApiModelProperty(value = "token id")
    @NotBlank
    private String tokenId;

    @ApiModelProperty(value = "资产权益详情列表")
    @NotEmpty
    private List<RequestRightDetail> rightDetails;


}
