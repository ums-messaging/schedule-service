package com.ums.schedule.domain.exception.email;

import com.ums.schedule.domain.exception.PolicyViolationException;

public abstract class EmailMessagePolicyViolationException extends PolicyViolationException {
    protected EmailMessagePolicyViolationException(String message) {
        super(message);
    }

    protected EmailMessagePolicyViolationException(String message, Throwable e) {
        super(message, e);
    }
}
