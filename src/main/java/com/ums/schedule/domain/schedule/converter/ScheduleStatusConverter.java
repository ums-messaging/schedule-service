package com.ums.schedule.domain.schedule.converter;

import com.ums.schedule.domain.schedule.code.ScheduleStatusEnum;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class ScheduleStatusConverter extends StatusStateConverter {
    public ScheduleStatusConverter() {
        super(ScheduleStatusEnum.class);
    }
}
