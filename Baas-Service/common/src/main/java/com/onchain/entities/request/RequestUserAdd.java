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
public class RequestUserAdd {
    @ApiModelProperty(value = "成员类型")
    @NotBlank
    private String role;

    @ApiModelProperty(value = "姓名")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "手机")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱")
    @NotBlank
    @Pattern(regexp = CommonConst.EMAIL_REGEX)
    private String email;

    @ApiModelProperty(value = "身份证号码")
    @NotBlank
    @Pattern(regexp = CommonConst.ID_NUMBER_REGEX)
    private String idNumber;

    @ApiModelProperty(value = "身份证正面")
    @NotBlank
    private String idaFileUuid;

    @ApiModelProperty(value = "身份证反面")
    @NotBlank
    private String idbFileUuid;
}
