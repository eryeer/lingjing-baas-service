package com.onchain.dna2explorer.model.response;

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
public class ResponseSummary {
    @ApiModelProperty(value = "统计时间")
    private Long summaryTime;
    @ApiModelProperty(value = "统计数量")
    private Long summaryCount;
}
