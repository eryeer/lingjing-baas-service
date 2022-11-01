package com.onchain.entities.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangeGasTransfer {
    @ApiModelProperty(value = "链账户id列表")
    @NotBlank
    private List<Long> ids;

    @ApiModelProperty(value = "是否可接收燃料")
    @NotBlank
    private Boolean isGasTransfer;
}
