package com.ums.schedule.domain.exception.validation;

import com.ums.schedule.domain.exception.DomainException;

public abstract class ValidationException extends DomainException {
    protected ValidationException(String message) {
        super(message);
    }
}
