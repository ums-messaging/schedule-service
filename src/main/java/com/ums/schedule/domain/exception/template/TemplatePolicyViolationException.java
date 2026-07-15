package com.ums.schedule.domain.exception.template;

import com.ums.schedule.domain.exception.PolicyViolationException;

public abstract class TemplatePolicyViolationException extends PolicyViolationException {
    protected TemplatePolicyViolationException(String message) {
        super(message);
    }

    public TemplatePolicyViolationException(String message, Throwable e) {
        super(message, e);
    }
}
