package com.ums.schedule.domain.exception.email;

import com.ums.schedule.domain.exception.DomainException;

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
