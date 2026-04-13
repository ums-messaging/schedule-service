package com.ums.schedule.application.schedule.factory.schedule_policy;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.SchedulePolicyValue;

import static com.ums.schedule.domain.schedule.code.ScheduleTypeEnum.RESERVATION;
import static java.time.LocalDateTime.now;

public class ReservationScheduleFactory implements SchedulePolicyFactory {
    @Override
    public boolean supports(EnumMapperValue scheduleType) {
        return scheduleType.code().equals(RESERVATION.code());
    }

    @Override
    public ScheduleCyclePolicy create(String cycleCd, String cycleValue) {
        SchedulePolicyValue value = ReservationPolicyValue.of(cycleValue);
        return ScheduleCyclePolicy.of(value);
    }
}
