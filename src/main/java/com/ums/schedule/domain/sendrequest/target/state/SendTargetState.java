package com.ums.schedule.domain.sendrequest.target.state;


import com.ums.schedule.common.code.target.SendTargetStatusEnum;

public interface SendTargetState {
    SendTargetStatusEnum currentStatusCode();
}
