package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface ScheduleStatus extends StatusState {
    ScheduleStatus onEvent(StatusStateEvent event);
    ScheduleState getCurrentCode();
    default boolean isRunning() {
        return getCurrentCode() == ScheduleState.RUNNING;
    }

    default boolean isInActive() {
        return getCurrentCode() == ScheduleState.INACTIVE;
    }

    default boolean isActive() {
        return getCurrentCode() == ScheduleState.ACTIVE;
    }
}
