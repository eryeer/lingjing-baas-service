package com.onchain.entities.request;

import com.onchain.constants.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestChangePassword {
    @ApiModelProperty(value = "用户id")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "原密码")
    @NotBlank
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank
    @Pattern(regexp = CommonConst.PASSWORD_REGEX)
    private String newPassword;

    @ApiModelProperty(value = "新密码确认")
    @NotBlank
    @Pattern(regexp = CommonConst.PASSWORD_REGEX)
    private String newPasswordConfirm;
}
