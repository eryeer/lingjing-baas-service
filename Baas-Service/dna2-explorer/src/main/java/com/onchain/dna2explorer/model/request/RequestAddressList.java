package com.onchain.dna2explorer.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RequestAddressList {
    @ApiModelProperty(value = "地址列表", required = true)
    @NotNull
    private List<String> addressList;
}