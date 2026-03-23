package com.ums.schedule.schedule.domain.status;

import com.ums.schedule.schedule.code.ScheduleStatusEnum;

import static com.ums.schedule.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleActiveStatus toActive() {
        throw changeToStatus("ACTIVE", "ACTIVE");
    }

    @Override
    public ScheduleStatus toRunning() {
        return new ScheduleRunningStatus();
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.ACTIVE;
    }

    @Override
    public ScheduleStatus toInActive() {
        return new ScheduleInActiveStatus();
    }

}
