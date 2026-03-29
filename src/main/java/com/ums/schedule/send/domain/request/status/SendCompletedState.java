package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SendCompletedStatusException;
import com.ums.schedule.send.domain.request.status.exception.SendErrorStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.COMPLETED;

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
