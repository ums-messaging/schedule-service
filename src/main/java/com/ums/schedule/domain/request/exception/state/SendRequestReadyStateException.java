package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestReadyStateException extends SendRequestStateException {
    protected SendRequestReadyStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestReadyStateException of(SendRequestStatusEnum to) {
        return new SendRequestReadyStateException(SendRequestStatusEnum.READY, to);
    }
}
