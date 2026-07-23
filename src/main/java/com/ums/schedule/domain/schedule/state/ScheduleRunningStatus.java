package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStateException;

public class ScheduleRunningStatus implements com.ums.schedule.domain.schedule.state.ScheduleStatus {

    @Override
    public com.ums.schedule.domain.schedule.state.ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEvent eventCode = ScheduleEvent.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                return new ScheduleActiveStatus();
            }
            case TO_RUNNING -> {
                throw InvalidScheduleStateException.of(ScheduleState.RUNNING, ScheduleState.RUNNING);
            }
            case TO_INACTIVE -> {
                throw InvalidScheduleStateException.of(ScheduleState.RUNNING, ScheduleState.INACTIVE);
            }
        }
        return null;
    }

    @Override
    public ScheduleState getCurrentCode() {
        return ScheduleState.RUNNING;
    }
}
