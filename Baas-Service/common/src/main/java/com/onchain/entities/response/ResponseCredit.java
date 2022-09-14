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
public class ResponseCredit {
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "客户编号")
    private String centerId;
    @ApiModelProperty(value = "授信状态")
    private String creditStatus;
    @ApiModelProperty(value = "授信额度")
    private Long creditAmount;
    @ApiModelProperty(value = "已使用授信额度")
    private Long useAmount;
    @ApiModelProperty(value = "额度开始日")
    private Date creditStartDate;
    @ApiModelProperty(value = "额度截止日")
    private Date creditEndDate;
}
