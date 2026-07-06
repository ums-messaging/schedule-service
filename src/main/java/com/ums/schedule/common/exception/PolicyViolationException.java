package com.ums.schedule.common.exception;

public abstract class PolicyViolationException extends DomainException {
    protected PolicyViolationException(String message) {
        super(message);
    }
}
