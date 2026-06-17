package com.ums.schedule.domain.sendrequest.template.exception;

public abstract class TemplateException extends RuntimeException {
    protected TemplateException(String message) {
        super(message);
    }
}
