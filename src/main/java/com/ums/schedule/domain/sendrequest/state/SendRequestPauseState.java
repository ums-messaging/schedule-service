package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.common.code.request.SendRequestEventEnum;
import com.ums.schedule.common.code.request.SendRequestStatusEnum;
import com.ums.schedule.domain.exception.request.InvalidSendRequestStateException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class SendRequestPauseState implements SendRequestState {
    @Override
    public SendRequestState onEvent(StatusStateEvent event) {
        SendRequestEventEnum eventType = SendRequestEventEnum.valueOf(event.code());

        switch (eventType) {
            case SEND_REQUEST_RESUME -> {
                return new SendRequestSendingState();
            }
            default -> {
                throw InvalidSendRequestStateException.of(getCurrentCode(), eventType);
            }
        }
    }

    @Override
    public SendRequestStatusEnum getCurrentCode() {
        return SendRequestStatusEnum.PAUSE;
    }
}
