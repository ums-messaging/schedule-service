package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.request.SendRequestStatusEnum;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

import static com.ums.schedule.common.code.request.SendRequestStatusEnum.COMPLETED;

public class SendRequestCompleteState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventCode = SendRequestEventEnum.valueOf(event.code());
        throw new InvalidSendRequestStateException(getCurrentCode(), eventCode);
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return COMPLETED;
    }
}
