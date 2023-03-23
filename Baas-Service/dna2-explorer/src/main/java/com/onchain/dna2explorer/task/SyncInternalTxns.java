package com.onchain.dna2explorer.task;


import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.lock.DistributedLockAnnotation;
import com.onchain.dna2explorer.mapper.TableHeightMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.service.InternalTxnsService;
import com.onchain.dna2explorer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import java.io.IOException;

import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSACTION_BLOCK_HEIGHT_FOR_INTERNAL_TXNS;
import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSACTION_BLOCK_HEIGHT_FOR_TRANSFER;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncInternalTxns {

    private final InternalTxnsService internalTxnsService;
    private final TransactionMapper transactionMapper;
    private final TableHeightMapper tableHeightMapper;

    @DistributedLockAnnotation("syncInternalTxns")
    @Scheduled(cron = "0/10 * * * * *")
    public void syncInternalTxns() throws Exception {
        log.info("Staring internal_txns sync");
        try {
            syncInternalTxnList();
        } catch (Exception e) {
            log.error("internal_txns exception occurred, error ...", e);
            throw e;
        }
    }

    private void syncInternalTxnList() throws Exception {
        Long startNumber = tableHeightMapper.getLastestTableHeightByTableName(TBL_TRANSACTION_BLOCK_HEIGHT_FOR_INTERNAL_TXNS);
        if (startNumber == null) {
            startNumber = 1L;
        }
        Long currentNumber = transactionMapper.getMaxBlockNumber();
        if (currentNumber == null) {
            return;
        }
        long epochCount = 0L;
        while (startNumber < currentNumber && epochCount < Constant.ExecuteLimit) {
            long endNumber = startNumber + Constant.SyncTransferLimit > currentNumber ?
                    currentNumber : startNumber + Constant.SyncTransferLimit;
            internalTxnsService.syncInternalTransaction(startNumber, endNumber);
            startNumber = endNumber;
            epochCount++;
        }
    }

}
