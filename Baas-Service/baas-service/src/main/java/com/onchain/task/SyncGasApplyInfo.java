package com.onchain.task;

import com.onchain.lock.DistributedLockAnnotation;
import com.onchain.service.GasService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SyncGasApplyInfo {
    final GasService gasService;

    @Scheduled(cron = "0/10 * * * * ? ")
    @DistributedLockAnnotation("syncGasApplyStatusAndUpdateSummary")
    public void syncGasApplyStatusAndUpdateSummary() {
        log.info("sync gas apply status task begin");
        try {
            gasService.syncGasApplyStatusAndUpdateSummary();
        } catch (Exception e) {
            log.error("sync gas apply status task error: ", e);
            throw e;
        }
        log.info("sync gas apply status task end");
    }

}
