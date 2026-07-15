package com.ums.schedule.domain.exception;

import com.ums.schedule.domain.exception.DomainException;

public class ResourceNotFoundException extends DomainException {
    protected ResourceNotFoundException(String resource) {
        super(String.format("%s is not found.", resource));
    }
}
