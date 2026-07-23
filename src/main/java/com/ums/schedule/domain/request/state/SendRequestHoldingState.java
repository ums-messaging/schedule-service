package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;

public class SendRequestHoldingState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEvent eventCode = SendRequestEvent.valueOf(event.code());

        switch (eventCode) {
            case SEND_REQUEST_UPDATED -> {
                return this;
            }
            case SEND_REQUEST_READY -> {
                return new SendRequestReadyState();
            }
            default ->
                throw InvalidSendRequestStateException.of(SendRequestStatus.HOLDING, eventCode.stateType());
        }
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return SendRequestStatus.HOLDING;
    }
}