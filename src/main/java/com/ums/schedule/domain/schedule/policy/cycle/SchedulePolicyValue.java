package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCdEnum;
import com.ums.schedule.common.code.schedule.ScheduleTypeEnum;

public interface SchedulePolicyValue {
    ScheduleTypeEnum getScheduleType();
    CycleCdEnum getCycleCdEnum();
    String getCycleValue();
}
