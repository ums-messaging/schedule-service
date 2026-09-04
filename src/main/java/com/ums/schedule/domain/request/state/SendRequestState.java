package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;

public interface SendRequestState extends StatusState {
    @Override
    SendRequestState onEvent(StatusStateEvent eventType);

    @Override
    SendRequestStatus getCurrentCode();

    default void validate() {
        switch (getCurrentCode()) {
            case SENDING -> throw InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_SENDING);
            case COMPLETED -> throw InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_COMPLETED);
            case REQUEST -> throw InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_REQUESTED);
            case ERROR -> throw InvalidSendRequestStateException.of(SendRequestErrorCode.SEND_REQUEST_FAILED);
        }
    }
}
