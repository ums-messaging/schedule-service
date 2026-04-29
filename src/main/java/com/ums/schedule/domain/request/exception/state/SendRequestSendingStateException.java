package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestSendingStateException extends SendRequestStateException {
    protected SendRequestSendingStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestSendingStateException of(SendRequestStatusEnum to) {
        return new SendRequestSendingStateException(SendRequestStatusEnum.SENDING, to);
    }
}
