package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
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
@ApiModel
public class ResponseCenterCompany {
    @ApiModelProperty(value = "核心企业id")
    private String centerId;
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "管理员姓名")
    private String userName;
    @ApiModelProperty(value = "管理员手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "开户银行")
    private String bankName;
    @ApiModelProperty(value = "银行账户")
    private String bankAccount;
}
