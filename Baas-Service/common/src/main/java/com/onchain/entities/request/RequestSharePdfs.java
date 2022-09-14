package com.onchain.entities.request;

import com.onchain.constants.CommonConst;
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
public class RequestSharePdfs {
    @ApiModelProperty(value = "文件哈希")
    @NotBlank
    private String fileHash;

    @ApiModelProperty(value = "共享用户手机号")
    @NotBlank
    @Pattern(regexp = CommonConst.PHONE_REGEX)
    private String phoneNumber;
}
