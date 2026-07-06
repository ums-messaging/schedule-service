package com.ums.schedule.domain.sendrequest.target.exeption;

public abstract class SendTargetException extends RuntimeException {
    public SendTargetException(String message) {
        super(message);
    }
}
