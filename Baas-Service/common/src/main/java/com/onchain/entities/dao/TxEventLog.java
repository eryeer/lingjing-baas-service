package com.onchain.entities.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onchain.util.TxAmountSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TxEventLog extends Base {
    /**
     * 交易hash值
     */
    private String txHash;

    /**
     * 区块链交易类型，208：部署合约交易 209：调用合约交易
     */
    private Integer txType;

    /**
     * 交易时间戳
     */
    private Long txTime;

    /**
     * 区块高度
     */
    private Integer blockHeight;

    /**
     * 交易在区块里的索引
     */
    private Integer blockIndex;

    /**
     * 交易手续费
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    private BigDecimal fee;

    /**
     * 交易落账标识  1：成功 0：失败
     */
    private Integer confirmFlag;

    /**
     * 交易的event log
     */
    private String eventLog;

    /**
     * 该交易真正调用的合约hash
     */
    private String calledContractHash;

    private String payer;

    @Builder
    public TxEventLog(String txHash, Integer txType, Long txTime, Integer blockHeight, Integer blockIndex, BigDecimal fee, Integer confirmFlag, String eventLog, String calledContractHash, String payer) {
        this.txHash = txHash;
        this.txType = txType;
        this.txTime = txTime;
        this.blockHeight = blockHeight;
        this.blockIndex = blockIndex;
        this.fee = fee;
        this.confirmFlag = confirmFlag;
        this.eventLog = eventLog;
        this.calledContractHash = calledContractHash;
        this.payer = payer;
    }
}