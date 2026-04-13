package com.ums.schedule.domain.channel.email.exception;

public abstract class TemplateException extends RuntimeException {
    protected TemplateException(String message) {
        super(message);
    }
}
