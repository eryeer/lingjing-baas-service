package com.onchain.dna2explorer.model.response;

import com.github.pagehelper.PageInfo;
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
@ApiModel(value = "合约的token持有详情")
public class ResponseNFTHolder {
    @ApiModelProperty(value = "nft合约地址")
    private String contractAddress;
    @ApiModelProperty(value = "持有人的总数")
    private Long holderCount;
    @ApiModelProperty(value = "某合约下的地址持有token的信息")
    private PageInfo<ResponseAccountNFTHolder> responseAccountNFTHolderPageInfo;
}
