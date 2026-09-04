package com.ums.schedule.application.schedule.exception;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.exception.DbNotFoundException;

public class ScheduleNotFoundException extends DbNotFoundException {
    protected ScheduleNotFoundException(Long id) {
        super(id, ScheduleErrorCode.NOT_FOUND_SCHEDULE);
    }

    public static ScheduleNotFoundException of(Long id) {
        return new ScheduleNotFoundException(id);
    }
}
