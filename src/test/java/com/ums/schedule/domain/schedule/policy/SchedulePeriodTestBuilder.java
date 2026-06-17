package com.ums.schedule.domain.schedule.policy;

import java.time.LocalDate;

public class SchedulePeriodTestBuilder {
    private LocalDate scheduleStartAt = LocalDate.now();
    private LocalDate scheduleEndAt = LocalDate.now().plusMonths(2);

    public static SchedulePeriodTestBuilder builder() {
        return new SchedulePeriodTestBuilder();
    }

    public SchedulePeriodTestBuilder scheduleStartAt(LocalDate scheduleStartAt) {
        this.scheduleStartAt = scheduleStartAt;
        return this;
    }

    public SchedulePeriodTestBuilder scheduleEndAt(LocalDate scheduleEndAt) {
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
