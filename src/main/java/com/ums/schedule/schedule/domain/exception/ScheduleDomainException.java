package com.ums.schedule.schedule.domain.exception;

public abstract class ScheduleDomainException extends RuntimeException {
    protected ScheduleDomainException(String message) {
        super(message);
    }


}
