package com.ums.schedule.domain.schedule_status;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.common.code.ScheduleTypeEnum;
import com.ums.schedule.domain.exception.InvalidScheduleStatusException;

import static com.ums.schedule.domain.exception.InvalidScheduleStatusException.changeToStatus;

public class ScheduleRunningStatus implements ScheduleStatus {

    @Override
    public ScheduleActiveStatus toActive() {
        return new ScheduleActiveStatus();
    }

    @Override
    public ScheduleStatus toRunning() {
        throw changeToStatus("RUNNING", "RUNNING");
    }

    @Override
    public ScheduleStatusEnum currentScheduleStatus() {
        return ScheduleStatusEnum.RUNNING;
    }

    @Override
    public ScheduleStatus toSending() {
        return new ScheduleSendingSchedule();
    }

    @Override
    public ScheduleStatus toPause() {
        throw changeToStatus("RUNNING", "PAUSE");
    }

    @Override
    public ScheduleStatus toStop() {
        throw changeToStatus("RUNNING", "STOP");
    }

    @Override
    public ScheduleStatus toInActive() {
        throw changeToStatus("RUNNING", "INACTIVE");
    }

    @Override
    public ScheduleStatus toCompleted() {
        throw changeToStatus("RUNNING", "COMPLETED");
    }
}
