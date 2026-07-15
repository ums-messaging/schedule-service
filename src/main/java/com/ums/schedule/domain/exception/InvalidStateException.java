package com.ums.schedule.domain.exception;

import com.ums.schedule.domain.exception.DomainException;

public abstract class InvalidStateException extends DomainException {
    public InvalidStateException(String message) {
        super(message);
    }

    protected InvalidStateException(String from, String to) {
        super("%s could not change status to %s. ".formatted(from, to));
    }
}
