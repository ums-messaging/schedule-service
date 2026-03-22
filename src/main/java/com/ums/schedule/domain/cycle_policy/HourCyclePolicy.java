package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;

public class HourCyclePolicy extends ScheduleCyclePolicy {

    protected HourCyclePolicy(int cycleValue) {
        super(CycleCdEnum.HOUR, String.valueOf(cycleValue));
    }

    @Override
    protected void validate() {
        int hour = Integer.parseInt(getCycleValue());
        if(hour < 0 || hour > 12) {
            throw InvalidCycleValueException.toHour();
        }
    }
}
