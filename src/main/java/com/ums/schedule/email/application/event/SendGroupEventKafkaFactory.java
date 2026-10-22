package com.ums.schedule.email.application.event;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetResultList;

public interface SendGroupEventKafkaFactory {
    boolean supports(EmailResultCode resultCode);
    SendTargetGroupEvent createGroupEvent(SendGroupEventType eventType, SendGroupEventResult result, SendTargetResultList resultList);
}
