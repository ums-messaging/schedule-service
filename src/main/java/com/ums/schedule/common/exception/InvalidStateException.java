package com.ums.schedule.common.exception;

public abstract class InvalidStateException extends DomainException {
    protected InvalidStateException(String from, String to) {
        super("%s could not change status to %s. ".formatted(from, to));
    }
}
