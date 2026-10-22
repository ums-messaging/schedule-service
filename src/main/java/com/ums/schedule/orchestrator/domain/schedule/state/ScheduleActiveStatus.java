package com.ums.schedule.orchestrator.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.orchestrator.domain.schedule.exception.InvalidScheduleStateException;

public class ScheduleActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEvent eventCode = ScheduleEvent.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                throw InvalidScheduleStateException.of(ScheduleState.ACTIVE, ScheduleState.ACTIVE);
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
    public ScheduleState getCurrentCode() {
        return ScheduleState.ACTIVE;
    }
}
