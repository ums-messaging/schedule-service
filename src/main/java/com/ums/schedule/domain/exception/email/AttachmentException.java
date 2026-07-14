package com.ums.schedule.domain.exception.email;

public abstract class AttachmentException extends RuntimeException {
    protected AttachmentException(String message) {
        super(message);
    }
}
