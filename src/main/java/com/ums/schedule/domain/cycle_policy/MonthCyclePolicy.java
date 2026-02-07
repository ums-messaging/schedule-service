package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.domain.exception.InvalidCycleValueException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.IntStream;

import static com.ums.schedule.common.code.CycleCdEnum.*;
import static java.util.Arrays.stream;

public class MonthCyclePolicy extends ScheduleCyclePolicy {

    protected MonthCyclePolicy(int cycleValue) {
        super(MONTH, String.valueOf(cycleValue));
    }

    @Override
    protected void validate() {
        int month = Integer.parseInt(getCycleValue());
        if(month < 1 || month > 12) {
            throw InvalidCycleValueException.toMonth();
        }
    }
}
