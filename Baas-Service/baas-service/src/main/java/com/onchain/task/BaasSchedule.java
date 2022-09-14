package com.onchain.task;

import com.onchain.service.CosService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class BaasSchedule {
    final CosService cosService;

    @Scheduled(cron = "0 0 0 * * ?")     // every day at 00:00
    @Transactional(rollbackFor = Exception.class)
    public void deleteCos() throws Exception {
        log.info("deleteCos task begin");
        try {
            cosService.deleteOutDateCos();
        } catch (Exception e) {
            log.error("deleteCos: ", e);
            throw e;
        }
        log.info("deleteCos task end");
    }
}
