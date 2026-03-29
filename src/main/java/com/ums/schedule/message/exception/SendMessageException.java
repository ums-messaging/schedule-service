package com.ums.schedule.message.exception;

public abstract class SendMessageException extends RuntimeException {
    protected SendMessageException(String message) {
        super(message);
    }
}
