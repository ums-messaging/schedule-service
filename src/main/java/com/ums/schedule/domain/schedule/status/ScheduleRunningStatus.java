package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;

public class ScheduleRunningStatus implements ScheduleStatus {

    @Override
    public ScheduleActiveStatus toActive() {
        return new ScheduleActiveStatus();
    }

    @Override
    public ScheduleStatus toRunning() {
        throw InvalidScheduleStatusException.changeToStatus("RUNNING", "RUNNING");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.RUNNING;
    }

    @Override
    public ScheduleStatus toInActive() {
        throw InvalidScheduleStatusException.changeToStatus("RUNNING", "INACTIVE");
    }

}
