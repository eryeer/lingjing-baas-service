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
public class ResponseWeAccess {
    @ApiModelProperty(value = "认证Token")
    private String access_token;

    @ApiModelProperty(value = "api ticket")
    private String ticket;

    @ApiModelProperty(value = "过期时间")
    private Long expires_in;

    @ApiModelProperty(value = "错误消息")
    private String errmsg;

    @ApiModelProperty(value = "错误码")
    private Integer errcode;
}
