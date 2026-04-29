package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestScheduleStateException extends SendRequestReadyStateException {
    protected SendRequestScheduleStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestScheduleStateException of(SendRequestStatusEnum to) {
        return new SendRequestScheduleStateException(SendRequestStatusEnum.SCHEDULED, to);
    }
}
