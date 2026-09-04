package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;

public class ScheduleCyclePolicyTestBuilder {
    private ScheduleType scheduleType = ScheduleType.REALTIME;
    private CycleCd cycleCd = CycleCd.ALWAYS;
    private String cycleValue = "";

    public static ScheduleCyclePolicyTestBuilder builder() {
        return new ScheduleCyclePolicyTestBuilder();
    }

    public ScheduleCyclePolicyTestBuilder scheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
        return this;
    }

    public ScheduleCyclePolicyTestBuilder cycleCd(CycleCd cycleCd) {
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
