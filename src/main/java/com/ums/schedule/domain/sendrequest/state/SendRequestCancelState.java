package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.domain.sendrequest.code.SendRequestEventEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.exception.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendRequestCancelState implements SendRequestState {
    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventCode = SendRequestEventEnum.valueOf(event.code());
        throw InvalidSendRequestStateException.of(getCurrentCode(), eventCode);
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return SendRequestStatusEnum.CANCEL;
    }
}
