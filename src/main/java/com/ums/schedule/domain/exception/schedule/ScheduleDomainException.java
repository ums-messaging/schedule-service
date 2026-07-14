package com.ums.schedule.domain.exception.schedule;

public abstract class ScheduleDomainException extends RuntimeException {
    protected ScheduleDomainException(String message) {
        super(message);
    }


}
