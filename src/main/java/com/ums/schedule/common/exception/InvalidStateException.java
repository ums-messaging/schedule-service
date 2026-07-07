package com.ums.schedule.common.exception;

public abstract class InvalidStateException extends DomainException {
    public InvalidStateException(String message) {
        super(message);
    }

    protected InvalidStateException(String from, String to) {
        super("%s could not change status to %s. ".formatted(from, to));
    }
}
