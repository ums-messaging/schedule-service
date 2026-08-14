package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetCreateContext;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.state.SendTargetFailState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final ApplicationEventPublisher publisher;
    private final SendTargetService targetService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SendTargetSaveResult upload(TargetUploadReport targetUploadReport, List<TargetMessageResult> results) {
        List<SendTarget> targetSaveResults = new ArrayList<>();
        List<SendTargetCreateContext> targetList = results.stream()
                .map(SendTargetCreateContext::of)
                .toList();
        long startMs = System.currentTimeMillis();
        try {
            targetSaveResults = targetService.saveTargetList(targetUploadReport, targetList);
        } catch (BusinessException e) {
            targetSaveResults = targetService.saveTarget(targetUploadReport, targetList);
        } finally {
            long endMs = System.currentTimeMillis();
            SendTargetSaveResult result = SendTargetSaveResult.of(targetSaveResults);
            SendTargetFailedEvent event = SendTargetFailedEvent.of(result);
            if(!event.failureTargetList().isEmpty()) {
                publisher.publishEvent(event);
            }
            return result;
        }
    }

}