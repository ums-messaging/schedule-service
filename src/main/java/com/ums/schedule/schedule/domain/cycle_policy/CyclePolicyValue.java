package com.ums.schedule.schedule.domain.cycle_policy;

import com.ums.schedule.schedule.code.CycleCdEnum;
import com.ums.schedule.schedule.code.ScheduleTypeEnum;
import lombok.Getter;


public class CyclePolicyValue implements SchedulePolicyValue {
    private CycleCdEnum cycleCd;
    @Getter
    private int cycle;

    public static CyclePolicyValue of(CycleCdEnum cycleCd, int cycle) {
       return new CyclePolicyValue(cycleCd, cycle);
    }

    private CyclePolicyValue(CycleCdEnum cycleCd, int cycle) {
        this.cycleCd = cycleCd;
        this.cycle = cycle;
    }


    @Override
    public ScheduleTypeEnum getScheduleType() {
        return ScheduleTypeEnum.CYCLE;
    }

    @Override
    public CycleCdEnum getCycleCdEnum() {
        return this.cycleCd;
    }

    @Override
    public String getCycleValue() {
        return String.valueOf(this.cycle);
    }
}
