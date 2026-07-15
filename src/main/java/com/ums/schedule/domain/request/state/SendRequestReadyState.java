package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;


public class SendRequestReadyState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEvent eventType = SendRequestEvent.valueOf(event.code());
        switch (eventType) {
            case SEND_REQUEST_UPDATED -> {
                return new SendRequestHoldingState();
            }
            case SEND_REQUEST_REQUESTED -> {
                return new SendRequestRequestState();
            }
            default -> throw InvalidSendRequestStateException.of(getCurrentCode(), eventType);
        }
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return SendRequestStatus.READY;
    }
}
