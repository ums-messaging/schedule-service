package com.ums.schedule.domain.request.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;

public record SendRequestCompletedEvent(
        Long requestId
) implements SendEvent {

    public static SendRequestCompletedEvent of(SendRequest sendRequest) {
        return new SendRequestCompletedEvent(sendRequest.getId());
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.SEND_ENDED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.COMPLETED;
    }


}
