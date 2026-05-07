package com.ums.schedule.fixture;

import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleDomainFixture {

    public static Schedule createSchedule() {
        SchedulePeriod period = ScheduleDomainFixture.createSchedulePeriod();
        ScheduleCyclePolicy policy = ScheduleDomainFixture.createReservationSchedulePolicy();
        return Schedule.of("schedule", period, policy);
    }

    public static ScheduleCyclePolicy createReservationSchedulePolicy() {
        LocalDateTime now = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);
        String reservationDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationDate);
        return ScheduleCyclePolicy.of(policyValue);
    }

    public static SchedulePeriod createSchedulePeriod() {
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusMonths(3);
        return SchedulePeriod.of(startAt, endAt);
    }
}
