package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendRequestSendingState implements SendRequestState {
    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEvent eventCode = SendRequestEvent.valueOf(event.code());
        switch (eventCode) {
            case SEND_REQUEST_PAUSE -> {
                return new SendRequestPauseState();
            }
            case SEND_REQUEST_SEND_COMPLETED -> {
                return new SendRequestCompleteState();
            }
            default -> throw InvalidSendRequestStateException.of(getCurrentCode(), eventCode);
        }
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return SendRequestStatus.SENDING;
    }
}
