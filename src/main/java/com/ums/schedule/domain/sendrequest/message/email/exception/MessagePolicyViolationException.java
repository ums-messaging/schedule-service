package com.ums.schedule.domain.sendrequest.message.email.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class MessagePolicyViolationException extends PolicyViolationException {
    protected MessagePolicyViolationException(String message) {
        super(message);
    }

}
