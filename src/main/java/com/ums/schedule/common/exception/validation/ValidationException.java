package com.ums.schedule.common.exception.validation;

import com.ums.schedule.common.exception.DomainException;

public abstract class ValidationException extends DomainException {
    protected ValidationException(String message) {
        super(message);
    }
}
