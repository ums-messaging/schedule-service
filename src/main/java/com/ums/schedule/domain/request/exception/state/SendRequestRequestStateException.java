package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestRequestStateException extends SendRequestStateException {
    protected SendRequestRequestStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestRequestStateException of(SendRequestStatusEnum to) {
        return new SendRequestRequestStateException(SendRequestStatusEnum.REQUEST, to);
    }
}
