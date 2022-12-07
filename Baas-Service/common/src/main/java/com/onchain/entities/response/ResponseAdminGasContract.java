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
public class ResponseAdminGasContract {
    @ApiModelProperty(value = "id")
    private Long Id;
    @ApiModelProperty(value = "合同文件的uuid")
    private String contractFileUUID;
    @ApiModelProperty(value = "合同文件")
    private ResponseCosFile contractFile;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "流水id")
    private String flowId;
    @ApiModelProperty(value = "审核状态")
    private int status;
    @ApiModelProperty(value = "签约的燃料数量")
    private String agreementAmount;
    @ApiModelProperty(value = "反馈意见")
    private String feedback;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "上传时间")
    private Long uploadTime;
    @ApiModelProperty(value = "审核完成时间")
    private Long approvedTime;
}
