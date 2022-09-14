package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserApprove {
    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "审批状态")
    @NotNull
    private Boolean isApproved;

    @ApiModelProperty(value = "审批反馈信息")
    private String approveFeedback;

}
