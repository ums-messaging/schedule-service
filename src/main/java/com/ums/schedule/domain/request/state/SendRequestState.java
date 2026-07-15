package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;

public interface SendRequestState extends StatusState {
    @Override
    SendRequestState onEvent(StatusStateEvent eventType);

    @Override
    SendRequestStatus getCurrentCode();

    default void validate() {
        switch (getCurrentCode()) {
            case SENDING, COMPLETED, REQUEST, ERROR ->
                throw InvalidSendRequestStateException.of(getCurrentCode());

        }
    }
}
