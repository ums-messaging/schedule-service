package com.ums.schedule.domain.schedule.cycle_policy;

import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;

public record ScheduleCyclePolicy(
        ScheduleTypeEnum scheduleType,
        CycleCdEnum cycleCd,
        SchedulePolicyValue policyValue
) {

    public static ScheduleCyclePolicy of(SchedulePolicyValue policyValue) {
        return new ScheduleCyclePolicy(policyValue.getScheduleType(),
                policyValue.getCycleCdEnum(),
                policyValue);
    }
}
