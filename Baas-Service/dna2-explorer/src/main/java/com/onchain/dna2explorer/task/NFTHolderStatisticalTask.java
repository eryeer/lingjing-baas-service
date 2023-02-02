package com.onchain.dna2explorer.task;

import com.onchain.dna2explorer.exception.TransferInformationException;
import com.onchain.dna2explorer.lock.DistributedLockAnnotation;
import com.onchain.dna2explorer.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.onchain.dna2explorer.constant.Constant.CountingNftHolderLimit;
import static com.onchain.dna2explorer.constant.Constant.ExecuteLimit;

@Slf4j
@Component
@AllArgsConstructor
public class NFTHolderStatisticalTask {
    final TokenService tokenService;

    @Scheduled(cron = "0/20 * * * * * ")
    @DistributedLockAnnotation("countNFTHolder")
    public void countNFTHolder() throws Exception {
        log.info("NFT Holders Statistical task begin");
        try {
            countingNFTHolderMulti(ExecuteLimit, CountingNftHolderLimit);
        }catch (TransferInformationException e){
            log.error("NFT transfer information error: ", e);
            throw e;
        } catch (Exception e) {
            log.error("NFT Holders statistical task error: ", e);
            throw e;
        }
        log.info("NFT Holders Statistical task end");
    }

    private void countingNFTHolderMulti(Long executeLimit, Long batchExecuteSize) throws Exception {
        long epochCount = 0L;
        while (epochCount < executeLimit) {
            tokenService.countingNFTHolder(batchExecuteSize);
            epochCount++;
        }
    }
}
