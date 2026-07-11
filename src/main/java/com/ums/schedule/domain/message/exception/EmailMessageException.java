package com.ums.schedule.domain.message.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class EmailMessageException extends PolicyViolationException {
    protected EmailMessageException(String message) {
        super(message);
    }
}
