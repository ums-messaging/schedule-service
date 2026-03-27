package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;

public class RequestStatus implements SendRequestStatus {

    @Override
    public SendRequestStatus toScheduled() {
        return new SchedulingStatus();
    }

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.REQUEST;
    }

}
