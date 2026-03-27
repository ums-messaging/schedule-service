package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.SendingStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

public class SendingStatus implements SendRequestStatus {

    @Override
    public SendRequestStatus toComplete() {
        return new SendSuccessStatus();
    }

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SENDING;
    }
}
