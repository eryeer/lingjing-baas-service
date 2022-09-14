package com.onchain.entities.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:PanChunYu
 * @program: SCF-Service
 * @JdkVersion:JDK1.8 语言版本
 * @date: 2021-02-05 15:18
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCosFile {
    @ApiModelProperty(value = "上传用户id")
    private String userId;
    @ApiModelProperty(value = "文件唯一字符串标识")
    private String uuid;
    @ApiModelProperty(value = "文件后缀, jpg/png/pdf/...")
    private String fileSuffix;
    @ApiModelProperty(value = "文件大小, 字节数")
    private String fileLength;
    @ApiModelProperty(value = "bucket名称")
    private String bucketName;
    @ApiModelProperty(value = "原始文件名")
    private String fileName;
    @ApiModelProperty(value = "云服务key")
    private String fileKey;
    @ApiModelProperty(value = "是否临时文件")
    private String isTemp;
    @ApiModelProperty(value = "业务类型")
    private String fileType;
    @ApiModelProperty(value = "url")
    private String url;

}
