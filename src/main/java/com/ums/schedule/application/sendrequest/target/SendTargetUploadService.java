package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.TargetMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final ApplicationEventPublisher publisher;
    private final TargetMessageCreateService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TargetUploadResultList upload(List<TargetMessage> messages) {
        try {
            targetService.saveTargetList(messages);
        } catch (BusinessException e) {
            targetService.saveTarget(messages);
        } finally {
            SendTargetFailedEvent event = SendTargetFailedEvent.of(messages);
            if(!event.failureTargetList().isEmpty()) {
                publisher.publishEvent(event);
            }
            return TargetUploadResultList.of(messages);
        }
    }
}