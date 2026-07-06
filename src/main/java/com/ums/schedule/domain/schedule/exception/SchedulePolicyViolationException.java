package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public class SchedulePolicyViolationException extends PolicyViolationException {
    protected SchedulePolicyViolationException(String message) {
        super(message);
    }
}
