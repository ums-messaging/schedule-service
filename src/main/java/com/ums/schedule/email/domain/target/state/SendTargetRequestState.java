package com.ums.schedule.email.domain.target.state;

import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.email.common.code.SendTargetRequestStatusCode;

public interface SendTargetRequestState extends StatusState {
    SendTargetRequestStatusCode currentStateCode();
}
