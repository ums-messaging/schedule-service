package com.ums.schedule.common.exception;

public class ResourceNotFoundException extends DomainException {
    protected ResourceNotFoundException(String resource) {
        super(String.format("%s is not found.", resource));
    }
}
