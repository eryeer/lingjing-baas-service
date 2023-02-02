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
public class ResponseTxSummary {
    @ApiModelProperty(value = "每日交易数量列表")
    private List<ResponseSummary> dailySummary;
    @ApiModelProperty(value = "每月交易数量列表")
    private List<ResponseSummary> monthlySummary;
}
