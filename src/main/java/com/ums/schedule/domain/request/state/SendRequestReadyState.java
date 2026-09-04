package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;

import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
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
            default -> throw InvalidSendRequestStateException.of(SendRequestStatus.READY, eventType.stateType());
        }
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return SendRequestStatus.READY;
    }
}
