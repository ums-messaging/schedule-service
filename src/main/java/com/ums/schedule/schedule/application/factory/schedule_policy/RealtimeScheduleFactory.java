package com.ums.schedule.schedule.application.factory.schedule_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;

import static com.ums.schedule.schedule.code.CycleCdEnum.ALWAYS;
import static com.ums.schedule.schedule.code.ScheduleTypeEnum.REALTIME;

public class RealtimeScheduleFactory implements SchedulePolicyFactory {

    @Override
    public boolean supports(EnumMapperValue ScheduleType) {
        return ScheduleType.code().equals(REALTIME.code());
    }

    @Override
    public ScheduleCyclePolicy create(String cycleCd, String cycleValue) {
        return new ScheduleCyclePolicy(REALTIME, ALWAYS, null);
    }
}
