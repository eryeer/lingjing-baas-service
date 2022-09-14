package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date:2021-02-22 17:18
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSupplierStatu {
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
    @ApiModelProperty(value = "审批状态")
    private String approveStatus;
    @ApiModelProperty(value = "审批反馈信息")
    private String approveFeedback;

}
