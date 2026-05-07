package com.ums.schedule.domain.schedule.converter;

import com.ums.schedule.code.EnumMapperConverter;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import jakarta.persistence.Converter;

@Converter
public class ScheduleStatusConverter extends EnumMapperConverter  {
    public ScheduleStatusConverter() {
        super(ScheduleStatusEnum.class);
    }
}
