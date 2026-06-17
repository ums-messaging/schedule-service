package com.ums.schedule.domain.schedule.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.domain.schedule.code.ScheduleTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class ScheduleTypeConverter extends EnumMapperConverter {
    public ScheduleTypeConverter() {
        super(ScheduleTypeEnum.class);
    }
}
