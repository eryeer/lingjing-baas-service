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
public class Block {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private Long blockNumber;
    private String blockHash;
    private Long blockTime;
    private String miner;
    private Long difficulty;
    private Long totalDifficulty;
    private Integer blockSize;
    private Long gasUsed;
    private Long gasLimit;
    private String nonce;
    private String extraData;
    private String uncleHash;
    private String parentHash;
    private String stateRoot;
    private String receiptsRoot;
    private String transactionsRoot;
    private Integer txCount;
}
