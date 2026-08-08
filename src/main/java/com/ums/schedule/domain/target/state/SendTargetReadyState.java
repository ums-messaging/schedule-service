package com.ums.schedule.domain.target.state;


import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendTargetReadyState implements SendTargetState {

    @Override
    public StatusState onEvent(StatusStateEvent event) {
        return null;
    }

    @Override
    public SendTargetStatus getCurrentCode() {
        return SendTargetStatus.READY;
    }
}
