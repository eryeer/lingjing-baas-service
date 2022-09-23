package com.onchain.entities.request;

import com.onchain.constants.CommonConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserApprove {
    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "审批状态")
    @NotNull
    @Pattern(regexp = CommonConst.APPROVE_STATUS)
    private String approveStatus;

    @ApiModelProperty(value = "审批反馈信息")
    private String approveFeedback;

}
