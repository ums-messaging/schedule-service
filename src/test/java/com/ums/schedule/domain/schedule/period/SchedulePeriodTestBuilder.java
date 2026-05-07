package com.ums.schedule.domain.schedule.period;

import java.time.LocalDateTime;

public class SchedulePeriodTestBuilder {
    private LocalDateTime scheduleStartAt = LocalDateTime.now();
    private LocalDateTime scheduleEndAt = LocalDateTime.now().plusMonths(2);

    public static SchedulePeriodTestBuilder builder() {
        return new SchedulePeriodTestBuilder();
    }

    public SchedulePeriodTestBuilder scheduleStartAt(LocalDateTime scheduleStartAt) {
        this.scheduleStartAt = scheduleStartAt;
        return this;
    }

    public SchedulePeriodTestBuilder scheduleEndAt(LocalDateTime scheduleEndAt) {
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
