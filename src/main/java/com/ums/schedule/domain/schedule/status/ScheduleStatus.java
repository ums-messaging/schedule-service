package com.ums.schedule.domain.schedule.status;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;

public interface ScheduleStatus {
    ScheduleActiveStatus toActive();
    ScheduleStatus toRunning();
    ScheduleStatus toInActive();
    ScheduleStatusEnum currentScheduleStatus();

    default void validateCurrentStatus() {
        if(currentScheduleStatus() != ScheduleStatusEnum.RUNNING) {
            throw InvalidScheduleStatusException.of();
        }
    }

}
