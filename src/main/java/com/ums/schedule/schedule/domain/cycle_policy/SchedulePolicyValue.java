package com.ums.schedule.schedule.domain.cycle_policy;

import com.ums.schedule.schedule.code.CycleCdEnum;
import com.ums.schedule.schedule.code.ScheduleTypeEnum;

public interface SchedulePolicyValue {
    ScheduleTypeEnum getScheduleType();
    CycleCdEnum getCycleCdEnum();
    String getCycleValue();
}
