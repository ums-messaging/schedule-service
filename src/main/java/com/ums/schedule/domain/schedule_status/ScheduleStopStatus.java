package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleStopStatus implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        throw changeToStatus("STOP", "ACTIVE");
    }

    @Override
    public ScheduleStatus toRunning() {
        throw changeToStatus("STOP", "RUNNING");
    }

    @Override
    public ScheduleStatus toSending() {
        throw changeToStatus("STOP", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("STOP", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        throw changeToStatus("STOP", "STOP");
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("STOP" , "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        return new ScheduleCompletedStatus();
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.STOP;
    }
}
