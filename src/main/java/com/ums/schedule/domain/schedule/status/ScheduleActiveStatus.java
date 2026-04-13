package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;

public class ScheduleActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleActiveStatus toActive() {
        throw InvalidScheduleStatusException.changeToStatus("ACTIVE", "ACTIVE");
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
