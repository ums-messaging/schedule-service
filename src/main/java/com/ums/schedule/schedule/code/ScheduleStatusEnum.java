package com.ums.schedule.schedule.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum ScheduleStatusEnum implements EnumMapperType {
    RUNNING("RUNNING","실행중"),
    ACTIVE("ACTIVE", "활성화"),
    INACTIVE("INACTIVE", "비활성화");

    String value;
    String description;

    ScheduleStatusEnum(String value, String description) {
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
