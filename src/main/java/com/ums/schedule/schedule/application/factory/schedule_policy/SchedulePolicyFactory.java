package com.ums.schedule.schedule.application.factory.schedule_policy;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;

public interface SchedulePolicyFactory extends EnumMapperSelector {
   ScheduleCyclePolicy create(String cycleCd, String cycleValue);
}
