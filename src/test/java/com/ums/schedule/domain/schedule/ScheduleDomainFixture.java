package com.ums.schedule.domain.schedule;

import com.ums.schedule.domain.schedule.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleDomainFixture {

    public static ScheduleCyclePolicy createReservationSchedulePolicy() {
        LocalDateTime now = LocalDateTime.now().plusDays(3).withSecond(0).withNano(0);
        String reservationDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

        ReservationPolicyValue policyValue = ReservationPolicyValue.of(reservationDate);
        return ScheduleCyclePolicy.of(policyValue);
    }
}
