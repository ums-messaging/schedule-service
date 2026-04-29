package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestCompleteStateException extends SendRequestStateException {
    protected SendRequestCompleteStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestCompleteStateException of(SendRequestStatusEnum to) {
        return new SendRequestCompleteStateException(SendRequestStatusEnum.COMPLETED, to);
    }
}
