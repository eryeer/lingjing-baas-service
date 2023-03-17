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
public class ResponseInternalTx {
    @ApiModelProperty(value = "Id")
    private Long Id;
    @ApiModelProperty(value = "调用类型")
    private String type;
    @ApiModelProperty(value = "发送燃料数量（gwei）")
    private String value;
    @ApiModelProperty(value = "调用发起地址")
    private String fromAddress;
    @ApiModelProperty(value = "被调用地址")
    private String toAddress;
    @ApiModelProperty(value = "燃料上限")
    private Long gas;
    @ApiModelProperty(value = "燃料使用量")
    private Long gasUsed;
    @ApiModelProperty(value = "输入数据")
    private String input;
    @ApiModelProperty(value = "输出数据")
    private String output;
    @ApiModelProperty(value = "错误消息")
    private String error;
    @ApiModelProperty(value = "父调用Id")
    private Long parentId;
    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "上链时间")
    private Long blockTime;
    @ApiModelProperty(value = "上链区块编号")
    private Long blockNumber;
    @ApiModelProperty(value = "子调用列表")
    private List<ResponseInternalTx> calls;
}
