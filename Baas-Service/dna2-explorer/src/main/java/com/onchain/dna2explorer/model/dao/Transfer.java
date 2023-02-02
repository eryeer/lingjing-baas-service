package com.onchain.dna2explorer.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String txHash;
    private String blockHash;
    private Long blockNumber;
    private String fromAddress;
    private String toAddress;
    private String contractAddress;
    private String transferFrom;
    private String transferTo;
    private Integer fromType;
    private Integer toType;
    private String tokenId;
    private Long blockTime;
    private Integer logIndex;
}
