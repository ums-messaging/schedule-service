package com.ums.schedule.application.schedule.factory.schedule_policy;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.policy.cycle.ReservationPolicyValue;
import com.ums.schedule.domain.schedule.policy.cycle.SchedulePolicyValue;

import static com.ums.schedule.common.code.schedule.ScheduleTypeEnum.RESERVATION;

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
