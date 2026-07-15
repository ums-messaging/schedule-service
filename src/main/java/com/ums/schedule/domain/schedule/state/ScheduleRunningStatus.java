package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.common.converter.StatusStateEvent;

public class ScheduleRunningStatus implements com.ums.schedule.domain.schedule.state.ScheduleStatus {

    @Override
    public com.ums.schedule.domain.schedule.state.ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEvent eventCode = ScheduleEvent.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                return new ScheduleActiveStatus();
            }
            case TO_RUNNING -> {
                throw InvalidScheduleStatusException.changeToStatus("RUNNING", "RUNNING");
            }
            case TO_INACTIVE -> {
                throw InvalidScheduleStatusException.changeToStatus("RUNNING", "INACTIVE");
            }
        }
        return null;
    }

    @Override
    public ScheduleStatus getCurrentCode() {
        return ScheduleStatus.RUNNING;
    }
}
