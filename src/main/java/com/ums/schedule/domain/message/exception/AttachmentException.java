package com.ums.schedule.domain.message.exception;

public abstract class AttachmentException extends RuntimeException {
    protected AttachmentException(String message) {
        super(message);
    }
}
