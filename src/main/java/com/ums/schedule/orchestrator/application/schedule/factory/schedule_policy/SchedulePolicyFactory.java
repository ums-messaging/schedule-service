package com.ums.schedule.orchestrator.application.schedule.factory.schedule_policy;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.orchestrator.domain.schedule.policy.cycle.ScheduleCyclePolicy;

public interface SchedulePolicyFactory extends EnumMapperSelector {
   ScheduleCyclePolicy create(String cycleCd, String cycleValue);
}
