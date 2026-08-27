package com.ums.schedule.domain.target.state;

import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.converter.state.StatusState;

public interface SendTargetState extends StatusState {
    @Override
    SendTargetStatus getCurrentCode();
}
