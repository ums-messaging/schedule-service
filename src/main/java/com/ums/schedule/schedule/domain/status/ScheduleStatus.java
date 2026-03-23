package com.ums.schedule.schedule.domain.status;


import com.ums.schedule.schedule.code.ScheduleStatusEnum;

public interface ScheduleStatus {
    ScheduleActiveStatus toActive();
    ScheduleStatus toRunning();
    ScheduleStatus toInActive();
    ScheduleStatusEnum currentScheduleStatus();
}
