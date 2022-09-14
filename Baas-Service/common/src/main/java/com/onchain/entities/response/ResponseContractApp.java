package com.onchain.entities.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseContractApp {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "应用名称")
    private String appName;
    @ApiModelProperty(value = "部署状态")
    private String contractStatus;
    @ApiModelProperty(value = "模板类型")
    private String templateType;
    @ApiModelProperty(value = "部署时间")
    private Date deployTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "部署历史")
    private String deployHistory;
    @ApiModelProperty(value = "部署文件id列表")
    private String contractFileUuids;
    @ApiModelProperty(value = "部署文件列表")
    private List<ResponseCosFile> contractFileList;
}
