package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponsePlatformInfo {
    @ApiModelProperty(value = "交易平台主体名称")
    private String platformName;

    @ApiModelProperty(value = "交易平台主体公钥")
    private String platformPubKey;
}
