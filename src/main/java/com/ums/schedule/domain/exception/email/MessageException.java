package com.ums.schedule.domain.exception.email;

import com.ums.schedule.domain.exception.DomainException;

public abstract class MessageException extends DomainException {
    public MessageException(String message) {
        super(message);
    }
}
