package com.ums.schedule.domain.schedule.code;

import com.ums.schedule.common.converter.StatusStateEvent;

public enum ScheduleEventEnum implements StatusStateEvent {
    TO_ACTIVE("active", "활성화"),
    TO_INACTIVE("inactvie", "비활성화"),
    TO_RUNNING("running", "실행중");

    String value;
    String description;

    ScheduleEventEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
