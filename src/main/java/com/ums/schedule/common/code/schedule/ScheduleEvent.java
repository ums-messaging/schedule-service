package com.ums.schedule.common.code.schedule;

import com.ums.schedule.common.converter.state.StatusStateEvent;

public enum ScheduleEvent implements StatusStateEvent {
    TO_ACTIVE(ScheduleState.ACTIVE, "활성화"),
    TO_INACTIVE(ScheduleState.INACTIVE, "비활성화"),
    TO_RUNNING(ScheduleState.RUNNING, "실행중");

    ScheduleState state;
    String description;

    ScheduleEvent(ScheduleState state, String description) {
        this.state = state;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.state.code();
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public ScheduleState stateType() {
        return ScheduleState.valueOf(this.state.code());
    }
}
