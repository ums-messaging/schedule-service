package com.ums.schedule.schedule.application.factory.cycle_policy;

import com.ums.schedule.common.code.EnumMapperSelector;
import com.ums.schedule.schedule.domain.cycle_policy.SchedulePolicyValue;
import com.ums.schedule.schedule.domain.exception.InvalidNumberFormatException;

public interface CyclePolicy extends EnumMapperSelector  {
    SchedulePolicyValue create(int cycleValue);
    default SchedulePolicyValue create(String cycleValue) {
        try {
            int cycle = Integer.parseInt(cycleValue);
            return create(cycle);
        } catch (NumberFormatException e) {
            throw InvalidNumberFormatException.ofCycleValue();
        }
    }
}
