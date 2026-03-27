package com.ums.schedule.send.domain.request.status;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.status.ScheduleStatus;
import com.ums.schedule.send.code.SendRequestStatusEnum;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.status.exception.SchedulingStatusException;

import static com.ums.schedule.send.code.SendRequestStatusEnum.*;

public class SchedulingStatus implements SendRequestStatus {


    @Override
    public SendRequestStatus toSend() {
        return new SendingStatus();
    }

    @Override
    public SendRequestStatusEnum currentSendRequestStatus() {
        return SendRequestStatusEnum.SCHEDULING;
    }

}
