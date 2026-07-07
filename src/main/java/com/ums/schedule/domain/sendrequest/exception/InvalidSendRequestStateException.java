package com.ums.schedule.domain.sendrequest.exception;

import com.ums.schedule.common.exception.InvalidStateException;
import com.ums.schedule.domain.sendrequest.code.SendRequestEventEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;

public class InvalidSendRequestStateException extends InvalidStateException {
    public InvalidSendRequestStateException(String message) {
        super(message);
    }

    public InvalidSendRequestStateException(SendRequestStatusEnum from, SendRequestEventEnum to) {
        super(from.code(), to.value());
    }

    private InvalidSendRequestStateException(SendRequestStatusEnum status) {
        super("%s 상태는 처리할 수 없습니다.".formatted(status.description()));
    }

    public static InvalidSendRequestStateException of(SendRequestStatusEnum from, SendRequestEventEnum to) {
        return new InvalidSendRequestStateException(from, to);
    }

    public static InvalidSendRequestStateException of(SendRequestStatusEnum status) {
        return new InvalidSendRequestStateException(status);
    }
}
