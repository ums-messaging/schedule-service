package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestErrorStateException extends SendRequestStateException {
    protected SendRequestErrorStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestErrorStateException of(SendRequestStatusEnum to) {
        return new SendRequestErrorStateException(SendRequestStatusEnum.ERROR, to);
    }
}
