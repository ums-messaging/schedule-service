package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.event.SendRequestStartedEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestScheduleStateException;

public class SendRequestScheduleState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SCHEDULED;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.SEND_STARTED) {
           return new SendRequestSendingState();
       }

       throw SendRequestScheduleStateException.of(sendEvent.getRequestStatus());
    }
}
