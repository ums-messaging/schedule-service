package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.common.exception.PolicyViolationException;

public class InvalidSchedulePeriodException extends PolicyViolationException {
    protected InvalidSchedulePeriodException(Long id, ScheduleErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }
    protected InvalidSchedulePeriodException(ScheduleErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static InvalidSchedulePeriodException of(ScheduleErrorCode errorCode, Object... args) {
        return new InvalidSchedulePeriodException(errorCode, args);
    }

    public static InvalidSchedulePeriodException of(Long id, ScheduleErrorCode errorCode, Object... args) {
        return new InvalidSchedulePeriodException(id, errorCode, args);
    }

}
