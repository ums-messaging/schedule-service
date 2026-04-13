package com.ums.schedule.domain.request.status;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.event.SendRequestEvent;

public interface SendRequestState {
    SendRequestStatusEnum currentSendRequestStatus();
    SendRequestState onEvent(SendRequestEvent event);
}
