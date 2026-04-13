package com.ums.schedule.domain.channel.email.exception;

public abstract class AttachmentException extends RuntimeException {
    protected AttachmentException(String message) {
        super(message);
    }
}
