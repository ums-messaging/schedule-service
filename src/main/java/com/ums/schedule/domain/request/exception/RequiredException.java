package com.ums.schedule.domain.request.exception;

public abstract class RequiredException extends SendRequestException  {
    protected RequiredException(String message) {
        super(message);
    }
}
