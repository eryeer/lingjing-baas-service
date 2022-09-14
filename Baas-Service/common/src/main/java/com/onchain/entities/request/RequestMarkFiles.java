package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestMarkFiles {
    @ApiModelProperty(value = "标记非临时文件Id列表")
    @NotNull
    private List<String> linkFileIds;

    @ApiModelProperty(value = "文件删除Id列表")
    @NotNull
    private List<String> deleteFileIds;
}
