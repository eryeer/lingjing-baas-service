package com.onchain.entities.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserContractInfo {
    @ApiModelProperty(value = "链账户地址")
    private String chainAccountAddress;
    @ApiModelProperty(value = "合约地址")
    private String contractAddress;
    @ApiModelProperty(value = "交易数量")
    private Integer txCount;
    @ApiModelProperty(value = "部署时间")
    private Date deployTime;
}
