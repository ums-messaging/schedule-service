package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SendRequestStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

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
