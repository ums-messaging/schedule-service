package com.ums.schedule.domain.sendrequest.converter;

import com.ums.schedule.common.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class ScheduleStatusConverter extends StatusStateConverter {
    public ScheduleStatusConverter() {
        super(ScheduleStatusEnum.class);
    }
}
