package com.ums.schedule.domain.request.status;

import com.ums.schedule.domain.request.exception.status.SendCompletedStatusException;
import com.ums.schedule.domain.request.event.SendRequestEvent;

public class SendingState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SENDING;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case SEND_END -> {
                return new SendCompletedState();
            }
            case SENDING, MESSAGE_MAKING -> {
                return this;
            }
        }
        throw SendCompletedStatusException.of(currentSendRequestStatus().code());
    }
}
