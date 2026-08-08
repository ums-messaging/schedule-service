package com.ums.schedule.common.code.schedule;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum ScheduleCode implements EnumMapper  {
    CYCLE_CD(CycleCd.class),
    SCHEDULE_TYPE(ScheduleType.class),
    SCHEDULE_STATUS(ScheduleType.class),
    SCHEDULE_EVENT(ScheduleEvent.class),
    ;

    Class<? extends EnumMapperType> code;

    ScheduleCode(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String key() {
        return name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }
}
