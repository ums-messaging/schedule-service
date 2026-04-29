package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestReadyStateException;

import static com.ums.schedule.code.send.SendRequestStatusEnum.READY;

public class SendRequestReadyState implements SendRequestState {
    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return READY;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        SendRequestEventTypeEnum eventType = event.getEventType();
        if(eventType == SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST) {
            return new SendRequestHoldingState();
        } else if(eventType == SendRequestEventTypeEnum.SEND_REQUESTED) {
            return new SendRequestRequestState();
        }
        throw SendRequestReadyStateException.of(sendEvent.getRequestStatus());
    }
}
