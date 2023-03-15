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
public class InternalTxn {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String type;
    private String value;
    private String fromAddress;
    private String toAddress;
    private Long gas;
    private Long gasUsed;
    private String input;
    private String output;
    private String error;
    private String revertReason;
    private Long parentId;
    private String txHash;
    private Long blockTime;
    private Long blockNumber;
}
