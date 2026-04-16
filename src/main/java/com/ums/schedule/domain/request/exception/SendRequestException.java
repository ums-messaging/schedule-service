package com.ums.schedule.domain.request.exception;

public abstract class SendRequestException extends RuntimeException {
    protected SendRequestException(String message) {
        super(message);
    }
}
