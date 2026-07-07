package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.sendrequest.exception.InvalidSendRequestStateException;

public interface SendRequestState extends StatusState {
    @Override
    SendRequestState onEvent(StatusStateEvent eventType);

    @Override
    SendRequestStatusEnum getCurrentCode();

    default void validate() {
        switch (getCurrentCode()) {
            case SENDING, COMPLETED, REQUEST, ERROR ->
                throw InvalidSendRequestStateException.of(getCurrentCode());

        }
    }
}
