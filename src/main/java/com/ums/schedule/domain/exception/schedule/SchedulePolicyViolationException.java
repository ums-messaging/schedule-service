package com.ums.schedule.domain.exception.schedule;

import com.ums.schedule.domain.exception.PolicyViolationException;

public class SchedulePolicyViolationException extends PolicyViolationException {
    protected SchedulePolicyViolationException(String message) {
        super(message);
    }
}
