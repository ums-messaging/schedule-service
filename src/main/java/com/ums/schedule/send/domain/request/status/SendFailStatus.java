package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.SendErrorStatusException;

public class SendFailStatus implements SendRequestStatus {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.FAILED;
    }

    @Override
    public SendRequestStatus toError() {
        throw SendErrorStatusException.of(currentSendRequestStatus().code());
    }
}
