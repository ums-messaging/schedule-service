package com.ums.schedule.domain.request.exception;

import com.ums.schedule.domain.request.SendRequest;

public abstract class SendRequestException extends RuntimeException {
    protected SendRequestException(String message) {
        super(message);
    }
}
