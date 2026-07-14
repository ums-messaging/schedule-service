package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEventEnum;
import com.ums.schedule.common.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusStateEvent;


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
