package com.ums.schedule.domain.exception.validation;

public class InvalidFilenameValueException extends InvalidValueException {
    protected InvalidFilenameValueException() {
        super("file name is invalid.");
    }

    public static InvalidFilenameValueException of() {
        return new InvalidFilenameValueException();
    }
}
