package com.ums.schedule.domain.request.event;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;

public record SendRequestScheduledEvent(
        Long requestId
) implements SendEvent {

    public static SendRequestScheduledEvent of(SendRequest request) {
        SendRequestScheduledEvent event = new SendRequestScheduledEvent(request.getId());
        return event;
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.SCHEDULED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.SCHEDULED;
    }
}
