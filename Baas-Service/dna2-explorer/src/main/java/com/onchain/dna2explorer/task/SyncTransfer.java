package com.onchain.dna2explorer.task;

import com.onchain.dna2explorer.constant.Constant;
import com.onchain.dna2explorer.lock.DistributedLockAnnotation;
import com.onchain.dna2explorer.mapper.TableHeightMapper;
import com.onchain.dna2explorer.mapper.TransactionMapper;
import com.onchain.dna2explorer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.onchain.dna2explorer.constants.CommonConst.TBL_TRANSACTION_BLOCK_HEIGHT_FOR_TRANSFER;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncTransfer {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TableHeightMapper tableHeightMapper;

    @DistributedLockAnnotation("syncTransferLock")
    @Scheduled(cron = "0/10 * * * * *")
    public void syncTransfer() throws Exception {
        log.info("Staring transfer sync");
        try {
            syncTransferList();
        } catch (Exception e) {
            log.error("syncTransfer exception occurred, error ...", e);
            throw e;
        }
    }

    private void syncTransferList() {
        Long startNumber = tableHeightMapper.getLastestTableHeightByTableName(TBL_TRANSACTION_BLOCK_HEIGHT_FOR_TRANSFER);
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
            transactionService.syncTransferList(startNumber, endNumber);
            startNumber = endNumber;
            epochCount++;
        }
    }

}
