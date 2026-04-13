package com.ums.schedule.domain.request.status;

import com.ums.schedule.domain.request.exception.status.SchedulingStatusException;
import com.ums.schedule.domain.request.event.SendRequestEvent;

public class RequestState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.REQUEST;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case SCHEDULING -> {
                return new SchedulingState();
            }
        }
        throw SchedulingStatusException.of(currentSendRequestStatus().code());
    }
}
