package com.ums.schedule.adapter.api.schedule;

public record ScheduleCreateRequest(
        String scheduleName,
        String scheduleType,
        String cycleCd,
        String cycleValue,
        String scheduleStartAt,
        String scheduleEndAt
) {
}
