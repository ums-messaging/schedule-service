package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.domain.exception.InvalidCycleValueException;

import java.util.stream.IntStream;

public class MinuteCyclePolicy extends ScheduleCyclePolicy {

    protected MinuteCyclePolicy(int cycleValue) {
        super(CycleCdEnum.MINUTE, String.valueOf(cycleValue));
    }

    @Override
    protected void validate() {
        int minute = Integer.parseInt(getCycleValue());
        if(minute < 0 || minute > 59) {
            throw InvalidCycleValueException.toMinute();
        }
    }
}
