package com.ums.schedule.application.schedule.factory.schedule_policy;

import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;

public interface SchedulePolicyFactory extends EnumMapperSelector {
   ScheduleCyclePolicy create(String cycleCd, String cycleValue);
}
