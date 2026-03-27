package com.ums.schedule.send.domain.target.status;

import com.ums.schedule.send.code.SendTargetStatusEnum;

public class SendTargetReadyStatus implements SendTargetStatus {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.READY;
    }
}
