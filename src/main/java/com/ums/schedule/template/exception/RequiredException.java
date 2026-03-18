package com.ums.schedule.template.exception;

public abstract class RequiredException extends TemplateException {
    protected RequiredException(String message) {
        super(String.format("%s is required.", message));
    }
}
