package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date:2021-02-05 18:05
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCenterCompanyList {
    @ApiModelProperty(value = "核心企业id/客户id")
    private String centerId;
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "管理员名称")
    private String userName;
    @ApiModelProperty(value = "管理员手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "注册状态")
    private Boolean isRegistered;
    @ApiModelProperty(value = "运营状态")
    private String status;

}
