package com.ums.schedule.attachment.exception;

public abstract class RequiredException extends AttachmentException {
    protected RequiredException(String message) {
        super(String.format("%s is required.", message));
    }
}
