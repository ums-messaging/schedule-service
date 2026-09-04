package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;

public interface SchedulePolicyValue {
    ScheduleType getScheduleType();
    CycleCd getCycleCdEnum();
    String getCycleValue();
}
