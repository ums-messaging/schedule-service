package com.ums.schedule.domain.schedule.converter;

import com.ums.schedule.code.EnumMapperConverter;
import com.ums.schedule.code.EnumMapperType;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class ScheduleTypeConverter extends EnumMapperConverter {
    public ScheduleTypeConverter() {
        super(ScheduleTypeEnum.class);
    }
}
