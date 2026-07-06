package com.ums.schedule.fixture.schedule;

import com.ums.schedule.domain.schedule.policy.SchedulePeriod;

import java.time.LocalDate;

public class SchedulePeriodEntityBuilder {
    private LocalDate scheduleStartAt = LocalDate.now();
    private LocalDate scheduleEndAt = LocalDate.now().plusMonths(2);

    public static SchedulePeriodEntityBuilder builder() {
        return new SchedulePeriodEntityBuilder();
    }

    public SchedulePeriodEntityBuilder scheduleStartAt(LocalDate scheduleStartAt) {
        this.scheduleStartAt = scheduleStartAt;
        return this;
    }

    public SchedulePeriodEntityBuilder scheduleEndAt(LocalDate scheduleEndAt) {
        this.scheduleEndAt = scheduleEndAt;
        return this;
    }

    public SchedulePeriod build() {
        return new SchedulePeriod(
                this.scheduleStartAt,
                this.scheduleEndAt
        );
    }
}
