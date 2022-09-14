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
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户链账户地址")
    private String userAddress;
    @ApiModelProperty(value = "用户燃料余额")
    private String balance;
    @ApiModelProperty(value = "最近申领燃料时间")
    private Date applyTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "用户钱包文件uuid")
    private String walletFileUuid;
    @ApiModelProperty(value = "用户钱包文件uuid")
    private ResponseCosFile walletFile;
}
