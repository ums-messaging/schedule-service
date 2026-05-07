package com.ums.schedule.code.schedule;

import com.ums.schedule.code.EnumMapperType;
import com.ums.schedule.domain.state.StatusStateEvent;

public enum ScheduleEventEnum implements StatusStateEvent {
    TO_ACTIVE("active", ""),
    TO_INACTIVE("inactvie", ""),
    TO_RUNNING("running", "");

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
