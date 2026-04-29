package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestScheduledEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestRequestStateException;

public class SendRequestRequestState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.REQUEST;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.SCHEDULED) {
            return new SendRequestScheduleState();
        }
        throw SendRequestRequestStateException.of(sendEvent.getRequestStatus());
    }
}
