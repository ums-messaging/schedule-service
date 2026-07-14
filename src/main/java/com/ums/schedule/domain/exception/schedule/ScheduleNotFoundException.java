package com.ums.schedule.domain.exception.schedule;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class ScheduleNotFoundException extends ResourceNotFoundException {
    protected ScheduleNotFoundException() {
        super("Schedule");
    }

    public static ScheduleNotFoundException of() {
        return new ScheduleNotFoundException();
    }
}
