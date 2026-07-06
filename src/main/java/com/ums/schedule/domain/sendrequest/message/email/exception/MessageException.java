package com.ums.schedule.domain.sendrequest.message.email.exception;

import com.ums.schedule.common.exception.DomainException;

public abstract class MessageException extends DomainException {
    public MessageException(String message) {
        super(message);
    }
}
