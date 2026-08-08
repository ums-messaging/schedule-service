package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final ApplicationEventPublisher failTargetUploadPublisher;
    private final SendTargetService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SendTargetSaveResult> upload(List<SendTarget> targetList, int partitionSize) {
        List<SendTargetSaveResult> results = new ArrayList<>();

        try {
            Map<Integer, List<SendTarget>> groupedTargetMap = groupedSendTargetList(targetList, partitionSize);

            Set<Map.Entry<Integer, List<SendTarget>>> entries = groupedTargetMap.entrySet();

            for (Map.Entry<Integer, List<SendTarget>> entry : entries) {
                SendTargetSaveResult saveResult = null;
                try {
                    saveResult = targetService.saveTargetList(entry.getValue());
                } catch (DataIntegrityViolationException e) {
                    saveResult = targetService.saveTarget(entry.getValue());
                } finally {
                    results.add(saveResult);
                }
            }
        } catch (BusinessException e) {
            List<SendTarget> failures = targetList.stream()
                    .map(target -> target.onError(e.getMessage()))
                    .toList();
            results.add(SendTargetSaveResult.of(failures));
        } finally {
            SendTargetFailedEvent event = SendTargetFailedEvent.of(results);

            if(!event.failureTargetList().isEmpty()) {
                failTargetUploadPublisher.publishEvent(event);
            }
        }
        return results;
    }
    private Map<Integer, List<SendTarget>> groupedSendTargetList(List<SendTarget> targetList, int partitionSize) {
        return targetList.stream()
                .collect(Collectors.groupingBy(i -> (targetList.indexOf(i) / partitionSize)));
    }
}