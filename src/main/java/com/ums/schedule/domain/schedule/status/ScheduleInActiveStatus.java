package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;

public class ScheduleInActiveStatus implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        return new ScheduleActiveStatus();
    }

    @Override
    public ScheduleStatus toRunning() {
        throw InvalidScheduleStatusException.changeToStatus("INACTIVE", "RUNNING");
    }


    @Override
    public ScheduleStatus toInActive() {
        throw InvalidScheduleStatusException.changeToStatus("INACTIVE", "INACTIVE");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.INACTIVE;
    }

}
