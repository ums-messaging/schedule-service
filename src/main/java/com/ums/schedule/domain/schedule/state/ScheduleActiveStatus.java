package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class ScheduleActiveStatus implements com.ums.schedule.domain.schedule.state.ScheduleStatus {

    @Override
    public com.ums.schedule.domain.schedule.state.ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEvent eventCode = ScheduleEvent.valueOf(event.code());
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
    public ScheduleStatus getCurrentCode() {
        return ScheduleStatus.ACTIVE;
    }
}
