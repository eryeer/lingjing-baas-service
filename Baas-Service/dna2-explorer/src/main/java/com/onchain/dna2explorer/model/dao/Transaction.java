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
public class Transaction {
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
    private String txValue;
    // 0: fail, 1: success
    private String txStatus;
    private Long blockTime;
    private Integer nonce;
    private Integer txIndex;
    // 0: normal; 1: contract creation
    private Integer txType;
    private String data;
    private Long gasPrice;
    private Long gasLimit;
    private Long gasUsed;
}
