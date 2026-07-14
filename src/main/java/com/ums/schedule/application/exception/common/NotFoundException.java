package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public abstract class NotFoundException extends ApplicationException {
    protected NotFoundException(String message) {
        super("%s is not found.".formatted(message));
    }
}
