package com.ums.schedule.domain.message.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class EmailMessagePolicyViolationException extends PolicyViolationException {
    protected EmailMessagePolicyViolationException(String message) {
        super(message);
    }

    protected EmailMessagePolicyViolationException(String message, Throwable e) {
        super(message, e);
    }
}
