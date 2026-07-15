package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface ScheduleStatus extends StatusState {
    ScheduleStatus onEvent(StatusStateEvent event);
    com.ums.schedule.common.code.schedule.ScheduleStatus getCurrentCode();
    default boolean isRunning() {
        return getCurrentCode() == com.ums.schedule.common.code.schedule.ScheduleStatus.RUNNING;
    }

    default boolean isInActive() {
        return getCurrentCode() == com.ums.schedule.common.code.schedule.ScheduleStatus.INACTIVE;
    }

    default boolean isActive() {
        return getCurrentCode() == com.ums.schedule.common.code.schedule.ScheduleStatus.ACTIVE;
    }
}
