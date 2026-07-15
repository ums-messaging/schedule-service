package com.ums.schedule.domain.request.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.schedule.ScheduleType;
import jakarta.persistence.Converter;

@Converter
public class ScheduleTypeConverter extends EnumMapperConverter {
    public ScheduleTypeConverter() {
        super(ScheduleType.class);
    }
}
