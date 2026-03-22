package com.ums.schedule.attachment.exception;

public abstract class AttachmentException extends RuntimeException {
    protected AttachmentException(String message) {
        super(message);
    }
}
