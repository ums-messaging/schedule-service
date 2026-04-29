package com.ums.schedule.domain.request.exception.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;

public class SendRequestHoldingStateException extends SendRequestStateException {

    protected SendRequestHoldingStateException(SendRequestStatusEnum from, SendRequestStatusEnum to) {
        super(from, to);
    }

    public static SendRequestHoldingStateException of(SendRequestStatusEnum to) {
        return new SendRequestHoldingStateException(SendRequestStatusEnum.HOLDING, to);
    }
}
