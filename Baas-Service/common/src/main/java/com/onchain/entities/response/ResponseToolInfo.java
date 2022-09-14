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
public class ResponseToolInfo {
    @ApiModelProperty(value = "开发组件名称")
    private String toolName;

    @ApiModelProperty(value = "开发组件下载地址")
    private String toolUrl;

    @ApiModelProperty(value = "开发组件版本")
    private String version;
}
