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
public class ResponseLogin {
    @ApiModelProperty(value = "认证Token")
    private String accessToken;

    @ApiModelProperty(value = "重试Token")
    private String refreshToken;
}
