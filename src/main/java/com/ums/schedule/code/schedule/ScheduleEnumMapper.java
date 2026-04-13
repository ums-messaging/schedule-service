package com.ums.schedule.code.schedule;

import com.ums.schedule.code.EnumMapper;
import com.ums.schedule.code.EnumMapperType;

public enum ScheduleEnumMapper implements EnumMapper  {
    CYCLE_CD(CycleCdEnum.class),
    SCHEDULE_TYPE(ScheduleTypeEnum.class),
    SCHEDULE_STATUS(ScheduleTypeEnum.class)
    ;

    Class<? extends EnumMapperType> code;

    ScheduleEnumMapper(Class<? extends EnumMapperType> code) {
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
