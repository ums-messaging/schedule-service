package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SendingStatusException;

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
