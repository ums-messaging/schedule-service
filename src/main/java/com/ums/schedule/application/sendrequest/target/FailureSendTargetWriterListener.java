package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.domain.target.TargetMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@EnableAsync
@Component
@RequiredArgsConstructor
@Slf4j
public class FailureSendTargetWriterListener {
    private final TargetMessageJpaRepository repository;

    @Async("failureTargetUploadExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(SendTargetFailedEvent event) {
        log.info("=============================================");
        log.info("fail.count = {}", event.failureTargetList().size());
        try {
            repository.saveAll(event.failureTargetList());
        } catch (Exception e) {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            Sheet sheet = workbook.createSheet();
        }
        log.info("=============================================");
    }
}

