package com.ums.schedule.domain.sendrequest.target.exeption;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class SendTargetPolicyViolationException extends PolicyViolationException {
    public SendTargetPolicyViolationException(String message) {
        super(message);
    }
}
