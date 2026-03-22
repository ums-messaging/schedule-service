package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

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
    public ScheduleStatus toSending() {
        throw changeToStatus("ACTIVE", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("ACTIVE", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        throw changeToStatus("ACTIVE", "STOP");
    }

    @Override
    public ScheduleStatus toInActive() {
        return new ScheduleInActiveStatus();
    }

    @Override
    public ScheduleStatus toCompleted() {
        throw changeToStatus("ACTIVE", "COMPLETED");
    }
}
