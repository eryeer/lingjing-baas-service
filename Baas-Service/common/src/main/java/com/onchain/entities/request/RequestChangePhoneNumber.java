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
public class RequestChangePhoneNumber {
    @ApiModelProperty(value = "手机号")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;

    @ApiModelProperty(value = "验证码")
    @NotBlank
    private String code;
}
