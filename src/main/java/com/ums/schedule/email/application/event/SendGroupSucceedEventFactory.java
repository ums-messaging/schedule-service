package com.ums.schedule.email.application.event;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class SendGroupSucceedEventFactory implements SendGroupEventKafkaFactory {

    @Override
    public boolean supports(EmailResultCode resultCode) {
        return EmailResultCode.SUCCESS == resultCode;
    }

    @Override
    public SendTargetGroupEvent createGroupEvent(SendGroupEventType eventType, SendGroupEventResult result, SendTargetResultList resultList) {
        DomainGroupEvent groupEvent = DomainGroupEvent.of(resultList.sendRequestId(), eventType, resultList.domainGroup());
        return SendTargetGroupEvent.of(groupEvent, result, resultList);
    }
}
