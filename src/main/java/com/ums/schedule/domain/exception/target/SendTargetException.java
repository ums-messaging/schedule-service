package com.ums.schedule.domain.exception.target;

public abstract class SendTargetException extends RuntimeException {
    public SendTargetException(String message) {
        super(message);
    }
}
