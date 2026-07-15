package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import lombok.Getter;


public class CyclePolicyValue implements SchedulePolicyValue {
    private CycleCd cycleCd;
    @Getter
    private int cycle;

    public static CyclePolicyValue of(CycleCd cycleCd, int cycle) {
       return new CyclePolicyValue(cycleCd, cycle);
    }

    private CyclePolicyValue(CycleCd cycleCd, int cycle) {
        this.cycleCd = cycleCd;
        this.cycle = cycle;
    }

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.CYCLE;
    }

    @Override
    public CycleCd getCycleCdEnum() {
        return this.cycleCd;
    }

    @Override
    public String getCycleValue() {
        return String.valueOf(this.cycle);
    }
}
