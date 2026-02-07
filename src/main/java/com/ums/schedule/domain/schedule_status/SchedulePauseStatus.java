package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class SchedulePauseStatus implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        throw changeToStatus("PAUSE", "ACTIVE");
    }

    @Override
    public ScheduleStatus toRunning() {
        return new ScheduleRunningStatus();
    }

    @Override
    public ScheduleStatus toSending() {
        throw changeToStatus("PAUSE", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("PAUSE", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        return new ScheduleStopStatus();
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("PAUSE", "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        throw changeToStatus("PAUSE", "COMPLETED");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.PAUSE;
    }
}
