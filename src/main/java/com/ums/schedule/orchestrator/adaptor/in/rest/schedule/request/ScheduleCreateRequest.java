package com.ums.schedule.orchestrator.adaptor.in.rest.schedule.request;

public record ScheduleCreateRequest(
        String scheduleName,
        String scheduleType,
        String cycleCd,
        String cycleValue,
        String scheduleStartAt,
        String scheduleEndAt
) {
}
