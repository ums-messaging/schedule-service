package com.ums.schedule.application.exception.common;

import com.ums.schedule.application.exception.ApplicationException;

public abstract class ValidationException extends ApplicationException {
    protected ValidationException(String message) {
        super(message);
    }
}
