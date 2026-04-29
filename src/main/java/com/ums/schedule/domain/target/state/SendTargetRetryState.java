package com.ums.schedule.domain.target.state;

import com.ums.schedule.code.send.SendTargetStatusEnum;

public class SendTargetRetryState implements SendTargetState {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.RETRYING;
    }
}
