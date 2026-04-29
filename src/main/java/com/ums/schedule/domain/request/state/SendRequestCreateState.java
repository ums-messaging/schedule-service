package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.request.exception.state.SendRequestCreateStateException;


import static com.ums.schedule.code.send.SendRequestStatusEnum.CREATE;


public class SendRequestCreateState implements SendRequestState {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return CREATE;
    }

    @Override
    public SendRequestState onEvent(SendRequestEvent event) {
        SendEvent sendEvent = event.getEvent();
        if(event.getEventType() == SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST) {
            return new SendRequestHoldingState();
        }
        throw SendRequestCreateStateException.of(sendEvent.getRequestStatus());
    }
}
