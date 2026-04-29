package com.ums.schedule.domain.target.state;


import com.ums.schedule.code.send.SendTargetStatusEnum;

public interface SendTargetState {
    SendTargetStatusEnum currentStatusCode();
}
