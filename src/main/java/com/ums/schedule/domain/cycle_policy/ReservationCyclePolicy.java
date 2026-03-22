package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationCyclePolicy extends ScheduleCyclePolicy {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected ReservationCyclePolicy(LocalDateTime cycleValue) {
        super(CycleCdEnum.ONCE, cycleValue.format(formatter));
    }

    @Override
    protected void validate() {

    }
}
