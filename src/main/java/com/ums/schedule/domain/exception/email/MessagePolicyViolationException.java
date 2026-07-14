package com.ums.schedule.domain.exception.email;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class MessagePolicyViolationException extends PolicyViolationException {
    protected MessagePolicyViolationException(String message) {
        super(message);
    }

}
