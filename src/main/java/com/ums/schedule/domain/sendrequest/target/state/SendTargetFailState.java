package com.ums.schedule.domain.sendrequest.target.state;

import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;

public class SendTargetFailState implements SendTargetState {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.FAIL;
    }
}
