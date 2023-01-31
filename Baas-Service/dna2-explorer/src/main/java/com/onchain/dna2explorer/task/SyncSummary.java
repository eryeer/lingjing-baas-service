package com.onchain.dna2explorer.task;

import com.onchain.dna2explorer.lock.DistributedLockAnnotation;
import com.onchain.dna2explorer.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncSummary {

    private final SummaryService summaryService;

    @DistributedLockAnnotation("nodeSyncLock")
    @Scheduled(cron = "0/60 * * * * *")
    @Transactional(rollbackFor = Exception.class)
    public void syncNode() {
        log.info("Staring node sync");
        try {
            summaryService.syncNodeInfo();
        } catch (Exception e) {
            log.error("SyncNode exception occurred, error ...", e);
            throw e;
        }
    }

    @Scheduled(cron = "0/60 * * * * *")
    @DistributedLockAnnotation("updateDailySummary")
    @Transactional(rollbackFor = Exception.class)
    public void updateDailySummary() {
        log.info("Updating daily information task begin");
        try {
            summaryService.updateDailySummary();
        } catch (Exception e) {
            log.error("updateDailyInfo: ", e);
            throw e;
        }
        log.info("Updating daily information task end");
    }

    @Scheduled(cron = "10/60 * * * * *")
    @DistributedLockAnnotation("updateMonthlySummary")
    @Transactional(rollbackFor = Exception.class)
    public void updateMonthlySummary() {
        log.info("Updating monthly information task begin");
        try {
            summaryService.updateMonthlySummary();
        } catch (Exception e) {
            log.error("updateDailyInfo: ", e);
            throw e;
        }
        log.info("Updating monthly information task end");
    }

}
