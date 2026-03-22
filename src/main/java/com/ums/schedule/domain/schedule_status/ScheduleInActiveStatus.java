package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

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
    public ScheduleStatus toSending() {
        throw changeToStatus("INACTIVE", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("INACTIVE", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        throw changeToStatus("INACTIVE", "STOP");
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("INACTIVE", "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        throw changeToStatus("INACTIVE", "COMPLETED");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.INACTIVE;
    }
}
