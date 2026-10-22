package com.ums.schedule.email.domain.target.state;

import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.email.common.code.SendTargetRequestStatusCode;

public class SendTargetCreateState implements SendTargetRequestState {
    @Override
    public SendTargetRequestStatusCode currentStateCode() {
        return SendTargetRequestStatusCode.CREATE;
    }

    @Override
    public StatusState onEvent(StatusStateEvent event) {
        return null;
    }

    @Override
    public EnumMapperType getCurrentCode() {
        return null;
    }
}
