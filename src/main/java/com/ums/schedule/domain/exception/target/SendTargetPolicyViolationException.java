package com.ums.schedule.domain.exception.target;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class SendTargetPolicyViolationException extends PolicyViolationException {
    protected SendTargetPolicyViolationException(String message) {
        super(message);
    }

    protected SendTargetPolicyViolationException(String message, Throwable e) {
        super(message, e);
    }

    protected SendTargetPolicyViolationException(Throwable e) {
        super(e);
    }
}
