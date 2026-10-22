package com.ums.schedule.orchestrator.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.orchestrator.domain.schedule.exception.InvalidScheduleStateException;


public class ScheduleInActiveStatus implements ScheduleStatus {

    @Override
    public ScheduleStatus onEvent(StatusStateEvent event) {
        ScheduleEvent eventCode = ScheduleEvent.valueOf(event.code());
        switch (eventCode) {
            case TO_ACTIVE -> {
                return new ScheduleActiveStatus();
            }
            case TO_RUNNING -> {
                throw InvalidScheduleStateException.of(ScheduleState.INACTIVE, ScheduleState.RUNNING);
            }
            case TO_INACTIVE -> {
                throw InvalidScheduleStateException.of(ScheduleState.INACTIVE, ScheduleState.INACTIVE);
            }
        }
        return null;
    }

    @Override
    public ScheduleState getCurrentCode() {
        return ScheduleState.INACTIVE;
    }
}
