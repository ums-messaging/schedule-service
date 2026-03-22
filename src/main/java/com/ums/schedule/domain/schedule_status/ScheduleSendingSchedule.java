package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleSendingSchedule implements ScheduleStatus {
    @Override
    public ScheduleActiveStatus toActive() {
        throw changeToStatus("SENDING", "ACTIVE");
    }

    @Override
    public ScheduleStatus toRunning() {
        throw changeToStatus("SENDING", "RUNNING");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.SENDING;
    }

    @Override
    public ScheduleStatus toSending() {
        throw changeToStatus("SENDING", "SENDING");
    }

    @Override
    public ScheduleStatus toPause() {
        return new SchedulePauseStatus();
    }

    @Override
    public ScheduleStatus toStop() {
        return new ScheduleStopStatus();
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("SENDING", "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        return new ScheduleCompletedStatus();
    }
}
