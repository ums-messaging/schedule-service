package com.ums.schedule.domain.exception.request;

import com.ums.schedule.domain.exception.InvalidStateException;
import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;

public class InvalidSendRequestStateException extends InvalidStateException {
    public InvalidSendRequestStateException(String message) {
        super(message);
    }

    public InvalidSendRequestStateException(SendRequestStatus from, SendRequestEvent to) {
        super(from.code(), to.value());
    }

    private InvalidSendRequestStateException(SendRequestStatus status) {
        super("%s 상태는 처리할 수 없습니다.".formatted(status.description()));
    }

    public static InvalidSendRequestStateException of(SendRequestStatus from, SendRequestEvent to) {
        return new InvalidSendRequestStateException(from, to);
    }

    public static InvalidSendRequestStateException of(SendRequestStatus status) {
        return new InvalidSendRequestStateException(status);
    }
}
