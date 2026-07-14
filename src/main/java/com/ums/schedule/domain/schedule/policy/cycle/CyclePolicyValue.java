package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCdEnum;
import com.ums.schedule.common.code.schedule.ScheduleTypeEnum;
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
