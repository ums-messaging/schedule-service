package com.ums.schedule.domain.exception.request;

public abstract class SendRequestException extends RuntimeException {
    protected SendRequestException(String message) {
        super(message);
    }
}
