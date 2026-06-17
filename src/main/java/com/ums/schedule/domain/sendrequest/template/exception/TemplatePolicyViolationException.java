package com.ums.schedule.domain.sendrequest.template.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class TemplatePolicyViolationException extends PolicyViolationException {
    protected TemplatePolicyViolationException(String message) {
        super(message);
    }

    public TemplatePolicyViolationException(String message, Throwable e) {
        super(message, e);
    }
}
