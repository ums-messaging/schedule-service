package com.ums.schedule.domain.message.exception;

import com.ums.schedule.common.exception.DomainException;

public abstract class EmailAttachmentException extends DomainException {
    public EmailAttachmentException(String message) {
        super(message);
    }

    public EmailAttachmentException(String message, Throwable e) {
        super(message, e);
    }

    public EmailAttachmentException(Throwable e) {
        super(e);
    }
}
