package com.ums.schedule.domain.sendrequest.state;

import com.ums.schedule.domain.sendrequest.code.SendRequestEventEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.exception.InvalidSendRequestStateException;
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
