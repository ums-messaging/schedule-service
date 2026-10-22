package com.ums.schedule.orchestrator.domain.event.converter;

import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.converter.state.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class ScheduleStatusConverter extends StatusStateConverter {
    public ScheduleStatusConverter() {
        super(ScheduleState.class);
    }
}
