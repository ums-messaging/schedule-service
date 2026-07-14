package com.ums.schedule.domain.schedule.state;

import com.ums.schedule.common.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface ScheduleStatus extends StatusState {
    ScheduleStatus onEvent(StatusStateEvent event);
    ScheduleStatusEnum getCurrentCode();
    default boolean isRunning() {
        return getCurrentCode() == ScheduleStatusEnum.RUNNING;
    }

    default boolean isInActive() {
        return getCurrentCode() == ScheduleStatusEnum.INACTIVE;
    }

    default boolean isActive() {
        return getCurrentCode() == ScheduleStatusEnum.ACTIVE;
    }
}
