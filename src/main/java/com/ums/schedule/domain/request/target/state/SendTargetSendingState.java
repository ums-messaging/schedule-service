package com.ums.schedule.domain.request.target.state;

import com.ums.schedule.common.code.target.SendTargetStatusEnum;

public class SendTargetSendingState implements SendTargetState {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return null;
    }
}
