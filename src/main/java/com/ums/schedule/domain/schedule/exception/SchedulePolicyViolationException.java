package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class SchedulePolicyViolationException extends PolicyViolationException {
    protected SchedulePolicyViolationException(Long id, ScheduleErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }

    protected SchedulePolicyViolationException(ScheduleErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static SchedulePolicyViolationException of(ScheduleErrorCode errorCode) {
        return new SchedulePolicyViolationException(errorCode);
    }

    public static SchedulePolicyViolationException of(Long id, ScheduleErrorCode errorCode) {
        return new SchedulePolicyViolationException(id, errorCode);
    }
}
