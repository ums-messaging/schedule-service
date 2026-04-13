package com.ums.schedule.domain.schedule.exception;

public abstract class ScheduleDomainException extends RuntimeException {
    protected ScheduleDomainException(String message) {
        super(message);
    }


}
