package com.ums.schedule.schedule.domain.status;

import com.ums.schedule.schedule.code.ScheduleStatusEnum;

import static com.ums.schedule.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleRunningStatus implements ScheduleStatus {

    @Override
    public ScheduleActiveStatus toActive() {
        return new ScheduleActiveStatus();
    }

    @Override
    public ScheduleStatus toRunning() {
        throw changeToStatus("RUNNING", "RUNNING");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.RUNNING;
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("RUNNING", "INACTIVE");
    }

}
