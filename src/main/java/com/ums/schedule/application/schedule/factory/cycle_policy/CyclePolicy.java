package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.common.code.mapper.EnumMapperSelector;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import com.ums.schedule.domain.schedule.policy.cycle.SchedulePolicyValue;

public interface CyclePolicy extends EnumMapperSelector  {
    SchedulePolicyValue create(int cycleValue);
    default SchedulePolicyValue create(String cycleValue) {
        try {
            int cycle = Integer.parseInt(cycleValue);
            return create(cycle);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
}
