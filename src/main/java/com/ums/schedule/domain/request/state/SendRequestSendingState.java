package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestCompletedEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestCompleteStateException;
import com.ums.schedule.domain.request.exception.state.SendRequestSendingStateException;

public class SendRequestSendingState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SENDING;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.SEND_ENDED) {
           return new SendRequestCompleteState();
       }
       throw SendRequestSendingStateException.of(sendEvent.getRequestStatus());
    }
}
