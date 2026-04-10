package com.ums.schedule.schedule.application.model.dto;

import com.ums.schedule.schedule.domain.Schedule;

public record ScheduleDto(
        String scheduleId,
        String cycleCd,
        String cycleValue,
        String schedulePeriod
) {
    public static ScheduleDto fromSchedule(Schedule schedule) {
        return null;
    }
}
