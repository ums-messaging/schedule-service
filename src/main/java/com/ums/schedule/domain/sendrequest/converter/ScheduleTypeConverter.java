package com.ums.schedule.domain.sendrequest.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.schedule.ScheduleTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class ScheduleTypeConverter extends EnumMapperConverter {
    public ScheduleTypeConverter() {
        super(ScheduleTypeEnum.class);
    }
}
