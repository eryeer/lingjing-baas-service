package com.onchain.entities.request;

import com.onchain.constants.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestLogin {
    @ApiModelProperty(value = "用户手机号")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX, message = "手机号格式错误")
    private String phoneNumber;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "登录验证码")
    @NotBlank
    private String loginCode;
}
