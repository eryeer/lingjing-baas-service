package com.onchain.entities.request;

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
public class RequestPrivateKeyCustody {
    @ApiModelProperty(value = "链账户id")
    @NotNull
    private Long chainAccountId;

    @ApiModelProperty(value = "链账户密钥")
    @NotBlank
    private String privateKey;


}
