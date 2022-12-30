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
public class RequestAssetCustomer {
    @ApiModelProperty(value = "客户唯一 id")
    @NotBlank
    private String customerId;

    @ApiModelProperty(value = "客户名称或昵称")
    @NotBlank
    private String customerName;

    @ApiModelProperty(value = "持有人钱包地址")
    @NotBlank
    private String ownerAddress;

    @ApiModelProperty(value = "资产详情列表")
    @NotEmpty
    private List<RequestAssetDetail> assetsDetails;


}
