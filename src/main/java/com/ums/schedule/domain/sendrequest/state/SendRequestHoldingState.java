package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.request.SendRequestStatusEnum;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendRequestHoldingState implements SendRequestState {

    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventType = SendRequestEventEnum.valueOf(event.code());

        switch (eventType) {
            case SEND_REQUEST_UPDATED -> {
                return this;
            }
            case SEND_REQUEST_READY -> {
                return new SendRequestReadyState();
            }
            default ->
                throw InvalidSendRequestStateException.of(getCurrentCode(), eventType);
        }
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return SendRequestStatusEnum.HOLDING;
    }
}