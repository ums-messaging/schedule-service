package com.ums.schedule.domain.sendrequest.resource.email.code;

public abstract class AttachmentException extends RuntimeException {
    protected AttachmentException(String message) {
        super(message);
    }
}
