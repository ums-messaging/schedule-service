package com.ums.schedule.schedule.application.command;

public record ScheduleCreateCommand(
        String scheduleName,
        String scheduleType,
        String cycleCd,
        String cycleValue,
        String scheduleStartAt,
        String scheduleEndAt
) {
}
