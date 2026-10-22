package com.ums.schedule.orchestrator.domain.event.converter;

import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.state.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class SendRequestStateConverter extends StatusStateConverter {
    public SendRequestStateConverter() {
        super(SendRequestStatus.class);
    }
}
