package com.ums.schedule.common.exception;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
    public DomainException(String message, Throwable e) {
        super(message, e);
    }
    public DomainException(Throwable e) {
        super(e);
    }
}
