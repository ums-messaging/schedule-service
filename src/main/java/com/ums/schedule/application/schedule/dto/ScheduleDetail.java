package com.ums.schedule.application.schedule.dto;

import java.time.LocalDateTime;

public record ScheduleDetail(
        Long scheduleId,
        String scheduleName,
        String schedulePeriod,
        ScheduleRequestReportDetail sendRequestReport,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
