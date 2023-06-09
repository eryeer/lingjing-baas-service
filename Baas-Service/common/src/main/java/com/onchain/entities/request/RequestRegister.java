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
public class RequestRegister {
    @ApiModelProperty(value = "手机号")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;

    @ApiModelProperty(value = "密码")
    @Pattern(regexp = CommonConst.PASSWORD_REGEX, message = "密码至少包含英文大小写及数字")
    private String password;

    @ApiModelProperty(value = "注册验证码")
    @NotBlank
    private String registerCode;
}
