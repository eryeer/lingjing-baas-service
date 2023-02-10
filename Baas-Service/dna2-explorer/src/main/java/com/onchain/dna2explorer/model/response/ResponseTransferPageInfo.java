package com.onchain.dna2explorer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onchain.entities.response.ResponseUserContractInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTransferPageInfo {
    @ApiModelProperty(value = "页码")
    private Integer pageNum;
    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
    @ApiModelProperty(value =  "记录总大小")
    private Integer total;
    @ApiModelProperty(value = "链账户的合约信息")
    private List<ResponseTransfer> transferList;
}
