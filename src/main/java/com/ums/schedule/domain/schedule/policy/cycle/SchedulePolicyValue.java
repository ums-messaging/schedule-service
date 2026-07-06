package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.domain.schedule.code.CycleCdEnum;
import com.ums.schedule.domain.schedule.code.ScheduleTypeEnum;

public interface SchedulePolicyValue {
    ScheduleTypeEnum getScheduleType();
    CycleCdEnum getCycleCdEnum();
    String getCycleValue();
}
