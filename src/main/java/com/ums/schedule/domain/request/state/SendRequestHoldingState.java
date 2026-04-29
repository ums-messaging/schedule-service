package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestHoldingStateException;

public class SendRequestHoldingState implements SendRequestState {
    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.HOLDING;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.TARGET_UPLOAD_COMPLETED) {
            return new SendRequestReadyState();
        }
        throw SendRequestHoldingStateException.of(sendEvent.getRequestStatus());
    }
}
