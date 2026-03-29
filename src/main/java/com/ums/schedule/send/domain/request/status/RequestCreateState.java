package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;
import com.ums.schedule.send.domain.request.status.exception.SendReadyStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

public class RequestCreateState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return CREATE;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        switch (event.getEventType()) {
            case MESSAGE_CREATED, TARGET_UPLOADED -> {
                if(event.getSendRequest().canTransitionToReady()) {
                    return new SendReadyState();
                }
                return this;
            }
            case JOB_CREATED -> {
                return this;
            }
        }
        throw SendReadyStatusException.of(currentSendRequestStatus().code());
    }
}
