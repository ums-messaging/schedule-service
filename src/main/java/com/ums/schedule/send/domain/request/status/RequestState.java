package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SchedulingStatusException;

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
