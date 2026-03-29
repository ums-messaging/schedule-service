package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestEventEnum;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.event.SendRequestEvent;

public interface SendRequestState {
    SendRequestStatusEnum currentSendRequestStatus();
    SendRequestState onEvent(SendRequestEvent event);
}
