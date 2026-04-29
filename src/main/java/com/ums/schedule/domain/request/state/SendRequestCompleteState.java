package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestCompleteStateException;

import static com.ums.schedule.code.send.SendRequestStatusEnum.COMPLETED;
import static com.ums.schedule.code.send.SendRequestStatusEnum.ERROR;


public class SendRequestCompleteState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return COMPLETED;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        throw SendRequestCompleteStateException.of(sendEvent.getRequestStatus());
    }

    @Override
    public SendRequestState toError() {
        throw SendRequestCompleteStateException.of(ERROR);
    }
}
