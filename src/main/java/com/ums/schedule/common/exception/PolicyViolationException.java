package com.ums.schedule.common.exception;

public abstract class PolicyViolationException extends DomainException {
    protected PolicyViolationException(String message) {
        super(message);
    }

    public PolicyViolationException(String message, Throwable e) {
        super(message, e);
    }

    public PolicyViolationException(Throwable e) {
        super(e);
    }
}
