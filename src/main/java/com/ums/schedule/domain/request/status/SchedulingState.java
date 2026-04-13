package com.ums.schedule.domain.request.status;

import com.ums.schedule.domain.request.exception.status.SendingStatusException;
import com.ums.schedule.domain.request.event.SendRequestEvent;

public class SchedulingState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SCHEDULED;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case SEND_START -> {
                return new SendingState();
            }
            case SCHEDULING -> {
                return this;
            }
        }
        throw SendingStatusException.of(currentSendRequestStatus().code());
    }
}
