package com.ums.schedule.domain.request.converter;

import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class ScheduleStatusConverter extends StatusStateConverter {
    public ScheduleStatusConverter() {
        super(ScheduleStatus.class);
    }
}
