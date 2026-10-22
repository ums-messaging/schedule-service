package com.ums.schedule.orchestrator.domain.event.state;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.orchestrator.domain.exception.InvalidSendRequestStateException;
import lombok.EqualsAndHashCode;

import static com.ums.schedule.common.code.request.SendRequestStatus.COMPLETED;

@EqualsAndHashCode
public class SendRequestCompleteState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEvent eventCode = SendRequestEvent.valueOf(event.code());
        throw InvalidSendRequestStateException.of(
                COMPLETED, eventCode.stateType());
    }

    @Override
    public SendRequestStatus getCurrentCode() {
        return COMPLETED;
    }
}
