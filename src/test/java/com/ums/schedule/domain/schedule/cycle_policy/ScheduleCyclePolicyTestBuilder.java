package com.ums.schedule.domain.schedule.cycle_policy;

import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;

public class ScheduleCyclePolicyTestBuilder {
    private ScheduleTypeEnum scheduleType = ScheduleTypeEnum.REALTIME;
    private CycleCdEnum cycleCd = CycleCdEnum.ALWAYS;
    private String cycleValue = "";

    public static ScheduleCyclePolicyTestBuilder builder() {
        return new ScheduleCyclePolicyTestBuilder();
    }

    public ScheduleCyclePolicyTestBuilder scheduleType(ScheduleTypeEnum scheduleType) {
        this.scheduleType = scheduleType;
        return this;
    }

    public ScheduleCyclePolicyTestBuilder cycleCd(CycleCdEnum cycleCd) {
        this.cycleCd = cycleCd;
        return this;
    }

    public ScheduleCyclePolicyTestBuilder cycleValue(String cycleValue) {
        this.cycleValue = cycleValue;
        return this;
    }

    public ScheduleCyclePolicy build() {
        return new ScheduleCyclePolicy(scheduleType, cycleCd, cycleValue, null);
    }
}
