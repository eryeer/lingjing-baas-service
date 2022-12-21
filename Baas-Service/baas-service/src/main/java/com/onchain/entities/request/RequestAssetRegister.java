package com.onchain.entities.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RequestAssetRegister {
    @ApiModelProperty(value = "元宇宙资产类型")
    @NotNull
    private Integer metaverseAssetType;

    @ApiModelProperty(value = "链id")
    @NotBlank
    private String chainId;

    @ApiModelProperty(value = "系列 id")
    @NotBlank
    private String seriesId;

    @ApiModelProperty(value = "系列名称")
    @NotBlank
    private String seriesName;

    @ApiModelProperty(value = "系列业务类别")
    @NotNull
    private Integer seriesBizType;

    @ApiModelProperty(value = "系列封面图")
    @NotBlank
    private String seriesCoverImgUrl;

    @ApiModelProperty(value = "系列描述")
    @NotBlank
    private String seriesDesc;

    @ApiModelProperty(value = "系列媒体形式")
    @NotNull
    private Integer seriesMediaType;

    @ApiModelProperty(value = "系列创建时间")
    @NotNull
    private Long createTime;

    @ApiModelProperty(value = "资产铸造数量")
    @NotNull
    private Long mintNumber;

    @ApiModelProperty(value = "合约地址")
    @NotBlank
    private String contractAddr;

    @ApiModelProperty(value = "链外元数据")
    @NotBlank
    private String metadataUrl;

    @ApiModelProperty(value = "发行方身份 id 类型")
    @NotNull
    private Integer issuerIdType;

    @ApiModelProperty(value = "发行方身份 id")
    @NotBlank
    private String issuerId;

    @ApiModelProperty(value = "发行方名称")
    @NotBlank
    private String issuerName;

    @ApiModelProperty(value = "品牌方身份 id 类型")
    @NotNull
    private Integer ipIdType;

    @ApiModelProperty(value = "品牌方身份 id")
    @NotBlank
    private String ipId;

    @ApiModelProperty(value = "品牌方名称")
    @NotBlank
    private String ipName;

    @ApiModelProperty(value = "流通方式")
    @NotNull
    private HashMap<String, String> circulationInfo;

}
