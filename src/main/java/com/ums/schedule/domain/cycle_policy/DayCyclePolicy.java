package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;

public class DayCyclePolicy extends ScheduleCyclePolicy {

    protected DayCyclePolicy(int cycleValue) {
        super(CycleCdEnum.DAY, String.valueOf(cycleValue));
    }

    @Override
    protected void validate() {
        int day = Integer.parseInt(getCycleValue());
        if(day < 1 || day > 31) {
            throw InvalidCycleValueException.toDay();
        }
    }
}