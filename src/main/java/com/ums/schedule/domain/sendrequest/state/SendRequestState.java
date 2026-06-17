package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface SendRequestState extends StatusState {
    @Override
    SendRequestState onEvent(StatusStateEvent eventType);

    @Override
    SendRequestStatusEnum getCurrentCode();
}
