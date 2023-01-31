package com.onchain.dna2explorer.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDownloadTx {
    @ApiModelProperty(value = "ticket", required = true)
    @NotBlank
    private String ticket;

    @ApiModelProperty(value = "randomStr", required = true)
    @NotBlank
    private String randomStr;

    @ApiModelProperty(value = "address", required = true)
    @NotBlank
    private String address;

    @ApiModelProperty(value = "startTime", required = true)
    @NotNull
    private Long startTime;

    @ApiModelProperty(value = "endTime", required = true)
    @NotNull
    private Long endTime;


}
