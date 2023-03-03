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
public class ResponseWeSign {
    @ApiModelProperty(value = "随机字符串")
    private String noncestr;

    @ApiModelProperty(value = "签名")
    private String signature;

    @ApiModelProperty(value = "时间戳")
    private String timestamp;
}
