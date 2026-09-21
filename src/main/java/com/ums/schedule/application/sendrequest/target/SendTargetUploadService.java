package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import com.ums.schedule.application.sendrequest.target.result.SendTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.application.target.exception.SendTargetUploadExcecption;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.TargetMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetUploadService {
    private final ApplicationEventPublisher publisher;
    private final TargetMessageCreateService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TargetUploadResultList upload(UUID uploadId, List<TargetMessage> messages) {
        try {
            targetService.saveTargetList(messages);
        } catch (DataIntegrityViolationException e) {
            throw SendTargetUploadExcecption.of(e);
        } catch (BusinessException e) {
            log.error("send.target.upload.service.error = {}", e.getErrorMessage());
            targetService.saveTarget(uploadId, messages);
        }
        SendTargetFailedEvent event = SendTargetFailedEvent.of(messages);
        if(!event.failureTargetList().isEmpty()) {
            publisher.publishEvent(event);
        }
        return TargetUploadResultList.of(messages);
    }
}