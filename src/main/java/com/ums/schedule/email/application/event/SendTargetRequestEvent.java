package com.ums.schedule.email.application.event;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import java.util.List;

public record SendTargetRequestEvent(
        Long requestId,
        SendGroupEventType eventType,
        SendGroupEventResult eventResult,
        List<SendTargetResultList> results
) {
    public static SendTargetRequestEvent of(Long sendRequestId, SendGroupEventType eventType, List<SendTargetResultList> results) {
        SendGroupEventResult result = SendGroupEventResult.of(EmailResultCode.SUCCESS, null);
        return new SendTargetRequestEvent(sendRequestId, eventType, result, results);
    }
}
