package com.ums.schedule.domain.state.schedule;

import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.state.StatusStateEvent;


public class ScheduleInActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEventEnum eventCode = ScheduleEventEnum.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                return new ScheduleActiveStatus();
            }
            case TO_RUNNING -> {
                throw InvalidScheduleStatusException.changeToStatus("INACTIVE", "RUNNING");
            }
            case TO_INACTIVE -> {
                throw InvalidScheduleStatusException.changeToStatus("INACTIVE", "INACTIVE");
            }
        }
        return null;
    }

    @Override
    public ScheduleStatusEnum getCurrentCode() {
        return ScheduleStatusEnum.INACTIVE;
    }
}
