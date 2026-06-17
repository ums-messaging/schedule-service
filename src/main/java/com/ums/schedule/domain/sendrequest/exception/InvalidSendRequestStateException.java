package com.ums.schedule.domain.sendrequest.exception;

import com.ums.schedule.common.exception.InvalidStateException;
import com.ums.schedule.domain.sendrequest.code.SendRequestEventEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;

public class InvalidSendRequestStateException extends InvalidStateException {
    public InvalidSendRequestStateException(SendRequestStatusEnum from, SendRequestEventEnum to) {
        super(from.code(), to.value());
    }

    public static InvalidSendRequestStateException of(SendRequestStatusEnum from, SendRequestEventEnum to) {
        return new InvalidSendRequestStateException(from, to);
    }
}
