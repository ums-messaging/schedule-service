package com.ums.schedule.application.schedule.dto;

public record ScheduleRequestReportDetail(
        Long totalCount,
        Long sendingCount,
        Long completedCount,
        Long readyCount
) {

}
