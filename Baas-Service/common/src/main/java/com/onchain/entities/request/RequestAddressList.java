package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RequestAddressList {
    @ApiModelProperty(value = "地址列表", required = true)
    @NotEmpty
    private List<String> addressList;
}