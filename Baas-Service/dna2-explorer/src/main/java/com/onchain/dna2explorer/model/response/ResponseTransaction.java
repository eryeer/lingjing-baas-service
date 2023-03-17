package com.onchain.dna2explorer.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ResponseTransaction {

    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "区块哈希")
    private String blockHash;
    @ApiModelProperty(value = "区块高度")
    private Long blockNumber;
    @ApiModelProperty(value = "发起地址")
    private String fromAddress;
    @ApiModelProperty(value = "接收地址")
    private String toAddress;
    @ApiModelProperty(value = "合约地址")
    private String contractAddress;
    @ApiModelProperty(value = "接收地址类型")
    private Integer toAddressType;
    @ApiModelProperty(value = "发送数量")
    private String txValue;
    @ApiModelProperty(value = "交易数据")
    private String data;
    @ApiModelProperty(value = "调用方法")
    private String method;
    // 0: fail, 1: success
    @ApiModelProperty(value = "交易状态")
    private String txStatus;
    // 0: normal; 1: contract creation
    @ApiModelProperty(value = "交易类型")
    private Integer txType;
    @ApiModelProperty(value = "生成时间")
    private Long blockTime;
    @ApiModelProperty(value = "Nonce")
    private Integer nonce;
    @ApiModelProperty(value = "交易序号")
    private Integer txIndex;
    @ApiModelProperty(value = "燃料上限")
    private Long gasLimit;
    @ApiModelProperty(value = "消耗燃料")
    private Long gasUsed;
    @ApiModelProperty(value = "燃料价格")
    private Long gasPrice;
    @ApiModelProperty(value = "日志列表")
    private List<ResponseTxLog> logList;
    @ApiModelProperty(value = "ERC转账列表")
    private List<ResponseTransferLog> ercTransferLog;
    @ApiModelProperty(value = "内部交易列表")
    private List<ResponseInternalTx> internalTxns;
}
