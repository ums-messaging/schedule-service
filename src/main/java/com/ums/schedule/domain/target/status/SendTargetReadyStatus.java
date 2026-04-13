package com.ums.schedule.domain.target.status;

import com.ums.schedule.SendTargetStatusEnum;

public class SendTargetReadyStatus implements SendTargetStatus {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.READY;
    }
}
