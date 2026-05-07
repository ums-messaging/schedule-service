package com.ums.schedule.code.schedule;

import com.ums.schedule.code.EnumMapperType;

public enum ScheduleTypeEnum implements EnumMapperType {
    REALTIME("RT", "실시간"),
    RESERVATION("RV", "예약"),
    CYCLE("CL", "주기");

    String value;
    String description;

    ScheduleTypeEnum(String value, String description) {
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
