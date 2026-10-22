package com.ums.schedule.orchestrator.application.schedule.factory.schedule_policy;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.domain.schedule.policy.cycle.ScheduleCyclePolicy;

import static com.ums.schedule.common.code.schedule.ScheduleType.REALTIME;

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
