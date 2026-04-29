package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;

public interface SendRequestState {
    SendRequestStatusEnum currentSendRequestStatus();
    SendRequestState onEvent(SendRequestEvent event);
    default SendRequestState toError() {
        return new SendRequestErrorState();
    }
}
