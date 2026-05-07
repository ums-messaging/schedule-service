package com.ums.schedule.application.schedule.dto;


public record ScheduleCreateCommand(
        String scheduleName,
        String scheduleStartAt,
        String scheduleEndAt,
        String createdBy
) {
}
