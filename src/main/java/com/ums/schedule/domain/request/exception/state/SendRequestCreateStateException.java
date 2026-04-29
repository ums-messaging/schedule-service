package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestCreateStateException extends SendRequestStateException {

    protected SendRequestCreateStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestCreateStateException of(SendRequestStatusEnum to) {
        return new SendRequestCreateStateException(SendRequestStatusEnum.CREATE, to);
    }
}
