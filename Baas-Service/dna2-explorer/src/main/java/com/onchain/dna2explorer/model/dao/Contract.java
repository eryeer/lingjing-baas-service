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
public class Contract {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String address;
    private String abi;
    private Long blockTime;
    private String creator;
    private String createTxHash;
    private String contractType;
    private String tokenName;
    private String tokenSymbol;
    private Integer decimals;
}
