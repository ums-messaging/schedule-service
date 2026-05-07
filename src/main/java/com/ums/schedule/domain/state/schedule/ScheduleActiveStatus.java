package com.ums.schedule.domain.state.schedule;

import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.state.StatusStateEvent;

public class ScheduleActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEventEnum eventCode = ScheduleEventEnum.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                throw InvalidScheduleStatusException.changeToStatus("ACTIVE", "ACTIVE");
            }
            case TO_RUNNING -> {
                return new ScheduleRunningStatus();
            }
            case TO_INACTIVE -> {
                return new ScheduleInActiveStatus();
            }
        }
        return null;
    }

    @Override
    public ScheduleStatusEnum getCurrentCode() {
        return ScheduleStatusEnum.ACTIVE;
    }
}
