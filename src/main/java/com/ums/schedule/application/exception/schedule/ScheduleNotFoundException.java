package com.ums.schedule.application.exception.schedule;

import com.ums.schedule.application.exception.common.NotFoundException;

public class ScheduleNotFoundException extends NotFoundException {
    protected ScheduleNotFoundException(Long scheduleId) {
        super("[%d] schedule ".formatted(scheduleId));
    }

    public static ScheduleNotFoundException of(Long scheduleId) {
        return new ScheduleNotFoundException(scheduleId);
    }
}
