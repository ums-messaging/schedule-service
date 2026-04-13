package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;

public interface ScheduleStatus {
    ScheduleActiveStatus toActive();
    ScheduleStatus toRunning();
    ScheduleStatus toInActive();
    ScheduleStatusEnum currentScheduleStatus();
}
