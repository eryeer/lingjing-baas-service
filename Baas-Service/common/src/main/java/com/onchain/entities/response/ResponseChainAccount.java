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
public class ResponseChainAccount {
    @ApiModelProperty(value = "链户id")
    private Long Id;
    @ApiModelProperty(value = "链户名称")
    private String name;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户链账户地址")
    private String userAddress;
    @ApiModelProperty(value = "链账户是否可以进行gas转账")
    private Boolean isGasTransfer;
    @ApiModelProperty(value = "链账户是否托管")
    private Boolean isCustody;
    @ApiModelProperty(value = "私钥")
    private String privateKey;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
