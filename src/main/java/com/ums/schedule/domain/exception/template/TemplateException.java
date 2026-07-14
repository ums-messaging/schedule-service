package com.ums.schedule.domain.exception.template;

public abstract class TemplateException extends RuntimeException {
    protected TemplateException(String message) {
        super(message);
    }
}
