package com.ums.schedule.send.domain.target.status;

import com.ums.schedule.send.code.SendTargetStatusEnum;

public class SendTargetCreatedStatus implements SendTargetStatus {
    @Override
    public SendTargetStatusEnum currentStatusCode() {
        return SendTargetStatusEnum.CREATED;
    }
}
