package com.ums.schedule.domain.request.event;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;

public record SendRequestStartedEvent(Long requestId) implements SendEvent {

    public static SendRequestStartedEvent of(SendRequest sendRequest) {
        return new SendRequestStartedEvent(sendRequest.getId());
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.SEND_STARTED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.SENDING;
    }
}
