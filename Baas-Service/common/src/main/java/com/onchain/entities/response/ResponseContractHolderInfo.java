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
public class ResponseContractHolderInfo {
    @ApiModelProperty(value = "页码")
    private Integer pageNum;
    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
    @ApiModelProperty(value =  "记录总大小")
    private Integer total;
    @ApiModelProperty(value = "链账户的合约信息")
    private List<ResponseUserContractInfo> userContractInfos;
    @ApiModelProperty(value = "部署的合约总数")
    private Integer userDeployedCount;

}
