package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SendCompletedStatusException;

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
