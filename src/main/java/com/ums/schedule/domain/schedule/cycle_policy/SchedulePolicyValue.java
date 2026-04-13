package com.ums.schedule.domain.schedule.cycle_policy;

import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;

public interface SchedulePolicyValue {
    ScheduleTypeEnum getScheduleType();
    CycleCdEnum getCycleCdEnum();
    String getCycleValue();
}
