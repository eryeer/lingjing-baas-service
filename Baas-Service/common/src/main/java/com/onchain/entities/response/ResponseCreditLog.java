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
public class ResponseCreditLog {
    @ApiModelProperty(value = "受理编号")
    private String creditNumber;
    @ApiModelProperty(value = "核心企业名称")
    private String centerName;
    @ApiModelProperty(value = "客户编号")
    private String centerId;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "经办人")
    private String operatorName;
    @ApiModelProperty(value = "额度开始日")
    private Date creditDate;
}
