package com.ums.schedule.domain.sendrequest.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class SendRequestPolicyException extends PolicyViolationException {
    public SendRequestPolicyException(String message) {
        super(message);
    }
}
