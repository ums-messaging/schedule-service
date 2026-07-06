package com.ums.schedule.domain.sendrequest.exception;

public abstract class SendRequestException extends RuntimeException {
    protected SendRequestException(String message) {
        super(message);
    }
}
