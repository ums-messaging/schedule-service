package com.ums.schedule.schedule.code;

import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperType;

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
