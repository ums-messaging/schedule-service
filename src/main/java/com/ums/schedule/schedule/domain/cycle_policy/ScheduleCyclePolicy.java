package com.ums.schedule.schedule.domain.cycle_policy;

import com.ums.schedule.schedule.code.CycleCdEnum;
import com.ums.schedule.schedule.code.ScheduleTypeEnum;

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
