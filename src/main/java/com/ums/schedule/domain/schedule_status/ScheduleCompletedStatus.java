package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleCompletedStatus implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        throw changeToStatus("COMPLETED", "ACTIVE");
    }

    @Override
    public ScheduleStatus toRunning() {
        return new ScheduleRunningStatus();
    }

    @Override
    public ScheduleStatus toSending() {
        throw changeToStatus("COMPLETED", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("COMPLETED", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        throw changeToStatus("COMPLETE", "STOP");
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("COMPLETED", "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        throw changeToStatus("COMPLETED", "COMPLETED");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.COMPLETED;
    }
}
