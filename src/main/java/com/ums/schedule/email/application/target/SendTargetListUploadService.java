package com.ums.schedule.email.application.target;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetListUploadService {
    private final SendGroupEventKafkaDispatcher eventDispatcher;
    private final SendTargetService targetService;

    public SendTargetResultList upload(Long sendRequestId, SendTargetGroupedList targetGroup) {
        SendTargetResultList resultList = null;
        log.info("group.target.list.upload, groupId = {}, size ={} ", targetGroup.group().groupId(), targetGroup.targets().size());
        try {
            List<SendTargetRequest> saved = targetService.saveTargetList(sendRequestId, targetGroup.targets());
            resultList = SendTargetResultList.of(sendRequestId, targetGroup.group(), saved);
            eventDispatcher.publish(SendGroupEventType.SEND_REQUEST_UPLOAD, SendGroupEventResult.of(), resultList);
        } catch (DataIntegrityViolationException e) {
            resultList = targetService.saveTarget(sendRequestId, targetGroup, e.getMessage());
            eventDispatcher.publish(SendGroupEventType.SEND_REQUEST_UPLOAD, SendGroupEventResult.of(EmailResultCode.SUCCESS, null), resultList);
        } catch (BusinessException e) {
            List<SendTargetError> errorList = getErrorList(targetGroup, e.getErrorMessage());
            resultList = SendTargetResultList.of(sendRequestId, targetGroup.group(), List.of(), errorList);
            eventDispatcher.publish(SendGroupEventType.SEND_REQUEST_UPLOAD, SendGroupEventResult.of(EmailResultCode.FAIL, e.getErrorMessage()), resultList);
        }
        return resultList;
    }

    private List<SendTargetError> getErrorList(SendTargetGroupedList targetGroup, String errorMessage) {
        return targetGroup.targets().stream()
                .map(target -> SendTargetError.of(target, SendTargetErrorTypeCode.UPLOAD_ERROR, errorMessage))
                .toList();
    }
}