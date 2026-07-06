package com.ums.schedule.application.exception;

public abstract class NotFoundException extends ApplicationException {
    protected NotFoundException(String message) {
        super("%s is not found.".formatted(message));
    }
}
