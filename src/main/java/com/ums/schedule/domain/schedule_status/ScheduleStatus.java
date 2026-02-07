package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;

public interface ScheduleStatus {
    ScheduleActiveStatus toActive();
    ScheduleStatus toRunning();
    ScheduleStatus toSending();
    ScheduleStatus toPause();
    ScheduleStatus toStop();
    ScheduleStatus toInActive();
    ScheduleStatus toCompleted();

    ScheduleStatusEnum currentScheduleStatus();
}
