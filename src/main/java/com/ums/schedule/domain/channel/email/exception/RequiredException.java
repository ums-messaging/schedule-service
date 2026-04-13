package com.ums.schedule.domain.channel.email.exception;

public abstract class RequiredException extends AttachmentException {
    protected RequiredException(String message) {
        super(String.format("%s is required.", message));
    }
}
