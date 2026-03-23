package com.ums.schedule.schedule.application.factory.schedule_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.schedule.domain.cycle_policy.ReservationPolicyValue;
import com.ums.schedule.schedule.domain.cycle_policy.SchedulePolicyValue;

import static com.ums.schedule.schedule.code.ScheduleTypeEnum.RESERVATION;
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
