package com.ums.schedule.domain.sendrequest.target.state;


import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;

public interface SendTargetState {
    SendTargetStatusEnum currentStatusCode();
}
