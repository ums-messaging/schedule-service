package com.ums.schedule.schedule.domain.status;

import com.ums.schedule.schedule.code.ScheduleStatusEnum;

import static com.ums.schedule.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleInActiveStatus implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        return new ScheduleActiveStatus();
    }

    @Override
    public ScheduleStatus toRunning() {
        throw changeToStatus("INACTIVE", "RUNNING");
    }


    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("INACTIVE", "INACTIVE");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.INACTIVE;
    }

}
