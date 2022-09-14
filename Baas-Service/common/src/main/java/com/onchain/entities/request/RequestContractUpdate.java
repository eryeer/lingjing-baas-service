package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContractUpdate {

    @ApiModelProperty(value = "应用名称")
    @NotBlank
    private String appName;

    @ApiModelProperty(value = "合约文件uuid列表")
    @NotEmpty
    private List<String> contractFileUuidList;
}
