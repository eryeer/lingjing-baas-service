package com.onchain.dna2explorer.model.response;

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
public class ResponseTxLog {

    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "日志索引")
    private Integer logIndex;
    @ApiModelProperty(value = "合约地址")
    private String address;
    @ApiModelProperty(value = "数据")
    private String data;
    @ApiModelProperty(value = "主题列表（json格式）")
    private String topics;
    @ApiModelProperty(value = "主题列表")
    private List<String> topicList;
    @ApiModelProperty(value = "事件名称")
    private String eventName;
}
