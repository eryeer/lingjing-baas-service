package com.onchain.dna2explorer.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RequestAddressList {
    @ApiModelProperty(value = "地址列表", required = true)
    @NotEmpty
    private List<String> addressList;
}