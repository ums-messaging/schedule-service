package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.exception.StateException;

public class InvalidScheduleStateException extends StateException {

    public InvalidScheduleStateException(ScheduleErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public InvalidScheduleStateException(ScheduleState from, ScheduleState to) {
        super(from, to);
    }

    public static InvalidScheduleStateException of(ScheduleState to, ScheduleState from) {
        return new InvalidScheduleStateException(from, to);
    }

    public static InvalidScheduleStateException of(Long id, ScheduleState state) {
        return new InvalidScheduleStateException(ScheduleErrorCode.STATE_TO_SCHEDULE, state.description(), id);
    }

}
