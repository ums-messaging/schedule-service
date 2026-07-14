package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.request.SendRequestStatusEnum;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendRequestRequestState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventCode = SendRequestEventEnum.valueOf(event.code());
        switch (eventCode) {
            case SEND_REQUEST_SEND_STARTED -> {
                return new SendRequestSendingState();
            }
            default -> throw InvalidSendRequestStateException.of(getCurrentCode(), eventCode);
        }
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return SendRequestStatusEnum.REQUEST;
    }
}
