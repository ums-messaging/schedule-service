package com.ums.schedule.domain.request.status;

import com.ums.schedule.domain.request.exception.status.SendCompletedStatusException;
import com.ums.schedule.domain.request.event.SendRequestEvent;

import static com.ums.schedule.domain.send.code.SendRequestStatusEnum.COMPLETED;

public class SendCompletedState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return COMPLETED;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case SEND_END -> {
                return this;
            }
        }
        throw SendCompletedStatusException.of(currentSendRequestStatus().code());
    }
}
