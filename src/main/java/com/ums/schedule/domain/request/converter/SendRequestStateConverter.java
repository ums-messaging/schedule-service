package com.ums.schedule.domain.request.converter;

import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class SendRequestStateConverter extends StatusStateConverter {
    public SendRequestStateConverter() {
        super(SendRequestStatus.class);
    }
}
