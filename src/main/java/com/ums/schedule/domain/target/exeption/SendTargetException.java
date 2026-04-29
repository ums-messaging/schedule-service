package com.ums.schedule.domain.target.exeption;

public abstract class SendTargetException extends RuntimeException {
    public SendTargetException(String message) {
        super(message);
    }
}
