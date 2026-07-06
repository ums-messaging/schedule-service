package com.ums.schedule.common.exception.validation;

public abstract class InvalidValueException extends ValidationException {
    protected InvalidValueException(String message) {
        super(message);
    }
}
