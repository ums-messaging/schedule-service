package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.code.EnumMapperSelector;
import com.ums.schedule.domain.schedule.cycle_policy.SchedulePolicyValue;
import com.ums.schedule.domain.schedule.exception.InvalidNumberFormatException;

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
