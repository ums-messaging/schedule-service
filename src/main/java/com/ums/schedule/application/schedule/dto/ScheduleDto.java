package com.ums.schedule.application.schedule.dto;

public record ScheduleDto(
        String scheduleId,
        String cycleCd,
        String cycleValue,
        String schedulePeriod
) {
}
