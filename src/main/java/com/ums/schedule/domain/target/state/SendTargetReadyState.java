package com.ums.schedule.domain.target.state;


import com.ums.schedule.common.code.target.SendTargetStatusEnum;

public class SendTargetReadyState implements SendTargetState {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.READY;
    }
}
