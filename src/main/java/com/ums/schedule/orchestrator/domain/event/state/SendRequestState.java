package com.ums.schedule.orchestrator.domain.event.state;

import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateEvent;

public interface SendRequestState extends StatusState {
    @Override
    SendRequestState onEvent(StatusStateEvent eventType);

    @Override
    SendRequestStatus getCurrentCode();
}
