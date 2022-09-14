package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContractDeploy {

    @ApiModelProperty(value = "应用名称")
    @NotBlank
    private String appName;

    @ApiModelProperty(value = "合约文件uuid")
    private String contractFileUuid;

    @ApiModelProperty(value = "合约名称")
    private String contractName;

    @ApiModelProperty(value = "合约字节码")
    private String bytecode;

    @ApiModelProperty(value = "部署参数")
    @NotNull
    private List<RequestContractParameter> constructorParameterList;
}
