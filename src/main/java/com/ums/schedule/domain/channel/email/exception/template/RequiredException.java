package com.ums.schedule.domain.channel.email.exception.template;

import com.ums.schedule.domain.channel.email.exception.TemplateException;

public abstract class RequiredException extends TemplateException {
    protected RequiredException(String message) {
        super(String.format("%s is required.", message));
    }
}
