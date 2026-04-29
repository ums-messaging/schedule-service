package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestErrorStateException;

public class SendRequestErrorState implements SendRequestState {
    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.ERROR;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST) {
            return new SendRequestHoldingState();
        }
        throw SendRequestErrorStateException.of(sendEvent.getRequestStatus());
    }

    @Override
    public SendRequestState toError() {
        throw SendRequestErrorStateException.of(SendRequestStatusEnum.ERROR);
    }
}
