package com.ums.schedule.application.schedule.factory.schedule_policy;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;

import static com.ums.schedule.code.schedule.CycleCdEnum.ALWAYS;
import static com.ums.schedule.code.schedule.ScheduleTypeEnum.REALTIME;

public class RealtimeScheduleFactory implements SchedulePolicyFactory {

    @Override
    public boolean supports(EnumMapperValue ScheduleType) {
        return ScheduleType.code().equals(REALTIME.code());
    }

    @Override
    public ScheduleCyclePolicy create(String cycleCd, String cycleValue) {
        return ScheduleCyclePolicy.realtimeOf();
    }
}
