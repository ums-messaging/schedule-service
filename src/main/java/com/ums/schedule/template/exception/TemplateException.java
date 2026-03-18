package com.ums.schedule.template.exception;

public abstract class TemplateException extends RuntimeException {
    protected TemplateException(String message) {
        super(message);
    }
}
