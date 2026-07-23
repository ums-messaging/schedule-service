package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class InvalidScheduleCyclePolicyException extends PolicyViolationException {
    public InvalidScheduleCyclePolicyException(ScheduleErrorCode errorCode) {
        super(errorCode);
    }

    public static InvalidScheduleCyclePolicyException of(ScheduleErrorCode errorCode) {
        return new InvalidScheduleCyclePolicyException(errorCode);
    }
}
