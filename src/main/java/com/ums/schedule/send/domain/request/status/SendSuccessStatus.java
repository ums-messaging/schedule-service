package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.status.exception.SendErrorStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.SUCCESS;

public class SendSuccessStatus implements SendRequestStatus {

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SUCCESS;
    }

    @Override
    public SendRequestStatus toError() {
        throw SendErrorStatusException.of(currentSendRequestStatus().code());
    }
}
