package com.ums.schedule.fixture.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduleCreateCommandBuilder {
    private String scheduleName;
    private String scheduleStartAt;
    private String scheduleEndAt;
    private String createdBy;

    public static ScheduleCreateCommandBuilder builder() {
        return new ScheduleCreateCommandBuilder();
    }

    private ScheduleCreateCommandBuilder() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.scheduleName = "실시간 스케쥴";
        this.scheduleStartAt = LocalDate.now().format(formatter);
        this.scheduleEndAt = LocalDate.now().plusDays(3).format(formatter);
        this.createdBy = "jang314";
    }

    public ScheduleCreateCommand build() {
        return new ScheduleCreateCommand(
                scheduleName,
                scheduleStartAt,
                scheduleEndAt,
                createdBy
        );
    }
}
