package com.ums.schedule.email.application.event;


import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class SendGroupFailureEventFactory implements SendGroupEventKafkaFactory {
    @Override
    public boolean supports(EmailResultCode resultCode) {
        return EmailResultCode.SUCCESS != resultCode;
    }

    @Override
    public SendTargetGroupEvent createGroupEvent(SendGroupEventType eventType, SendGroupEventResult result, SendTargetResultList resultList) {
        List<SendTargetError> errorList = Stream.concat(resultList.successList()
                        .stream()
                        .map(target -> SendTargetError.of(target, SendTargetErrorTypeCode.TARGET_REQUEST_ERROR, result.resultMessage())),
                resultList.errorList().stream()
        ).toList();

        SendTargetResultList results = SendTargetResultList.of(resultList.sendRequestId(), resultList.domainGroup(), List.of(), errorList);
        DomainGroupEvent groupEvent = DomainGroupEvent.of(resultList.sendRequestId(), eventType, resultList.domainGroup());

        return SendTargetGroupEvent.of(groupEvent, result, results);
    }

}
