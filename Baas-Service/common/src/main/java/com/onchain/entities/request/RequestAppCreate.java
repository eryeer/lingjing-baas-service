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
public class RequestAppCreate {

    @ApiModelProperty(value = "应用名称")
    @NotBlank
    private String appName;


    @ApiModelProperty(value = "模板类型")
    @NotBlank
    @Pattern(regexp = CommonConst.TEMP_TYPE_REGEX)
    private String templateType;
}
