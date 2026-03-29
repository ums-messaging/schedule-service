package com.ums.schedule.message.exception;

public abstract class MessageStateException extends SendMessageException {
    protected MessageStateException(String message) {
        super(message);
    }
}
