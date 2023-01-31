package com.onchain.dna2explorer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAddress {
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "地址类型")
    private Integer type;
    @ApiModelProperty(value = "燃料余额")
    private String balance;
    @ApiModelProperty(value = "生成时间")
    private Long blockTime;
    @ApiModelProperty(value = "交易数量")
    private Integer txCount;
    @ApiModelProperty(value = "创建合约数量")
    private Integer deployCount;

    // only for contract address
    @ApiModelProperty(value = "合约部署者")
    private String creator;
    @ApiModelProperty(value = "合约部署交易")
    private String createTxHash;
    @ApiModelProperty(value = "合约信息")
    private ResponseContract contractInfo;
    @ApiModelProperty(value = "合约发行的token持有者总数")
    private Long tokenHolderSum;
}
