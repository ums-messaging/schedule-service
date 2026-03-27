package com.ums.schedule.send.domain.exception;

public abstract class SendRequestException extends RuntimeException {
    public SendRequestException(String message) {
        super(message);
    }
}
