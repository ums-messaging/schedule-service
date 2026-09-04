package com.ums.schedule.domain.request.state;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.domain.request.exception.InvalidSendRequestStateException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SendRequestCancelState implements SendRequestState {
    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEvent eventCode = SendRequestEvent.valueOf(event.code());
        throw InvalidSendRequestStateException.of(SendRequestStatus.CANCEL, eventCode.stateType());
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return SendRequestStatus.CANCEL;
    }
}
