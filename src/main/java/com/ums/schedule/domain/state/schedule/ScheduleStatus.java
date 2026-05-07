package com.ums.schedule.domain.state.schedule;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.state.StatusState;
import com.ums.schedule.domain.state.StatusStateEvent;

public interface ScheduleStatus extends StatusState {
    ScheduleStatus onEvent(StatusStateEvent event);
    ScheduleStatusEnum getCurrentCode();
    default boolean isRunning() {
        return getCurrentCode() == ScheduleStatusEnum.RUNNING;
    }

    default boolean isInActive() {
        return getCurrentCode() == ScheduleStatusEnum.INACTIVE;
    }
}
