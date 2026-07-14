package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.request.SendRequestStatusEnum;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;


public class SendRequestReadyState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventType = SendRequestEventEnum.valueOf(event.code());
        switch (eventType) {
            case SEND_REQUEST_UPDATED -> {
                return new SendRequestHoldingState();
            }
            case SEND_REQUEST_REQUESTED -> {
                return new SendRequestRequestState();
            }
            default -> throw InvalidSendRequestStateException.of(getCurrentCode(), eventType);
        }
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return SendRequestStatusEnum.READY;
    }
}
