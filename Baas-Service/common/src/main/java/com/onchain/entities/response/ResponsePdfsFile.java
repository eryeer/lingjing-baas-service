package com.onchain.entities.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResponsePdfsFile {
    @ApiModelProperty(value = "上传者id")
    private String userId;
    @ApiModelProperty(value = "PDFS文件哈希")
    private String fileHash;
    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;
    @ApiModelProperty(value = "文件长度")
    private Long fileLength;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "上传者类型")
    private String uploaderType;
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;
}
