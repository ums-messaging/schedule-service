package com.ums.schedule.domain.exception.validation;

public abstract class InvalidValueException extends ValidationException {
    protected InvalidValueException(String message) {
        super(message);
    }
}
