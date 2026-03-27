package com.ums.schedule.send.domain.exception;

public abstract class RequiredException extends SendRequestException {
    public RequiredException(String message) {
        super(message);
    }
}
