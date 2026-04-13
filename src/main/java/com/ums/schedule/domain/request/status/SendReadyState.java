package com.ums.schedule.domain.request.status;

import com.ums.schedule.domain.request.exception.status.SendRequestStatusException;
import com.ums.schedule.domain.request.event.SendRequestEvent;

import static com.ums.schedule.domain.send.code.SendRequestStatusEnum.*;

public class SendReadyState implements SendRequestState {
    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return READY;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case SEND_REQUEST -> {
                return new RequestState();
            }
            case TARGET_UPLOADED, MESSAGE_CREATED -> {
                return this;
            }
        }
        throw SendRequestStatusException.of(currentSendRequestStatus().code());
    }


}
