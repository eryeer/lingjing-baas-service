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
public class ResponseFile {

    @ApiModelProperty(value = "原始文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件uuid")
    private String uuid;

    @ApiModelProperty(value = "文件临时url")
    private String tempUrl;
}
