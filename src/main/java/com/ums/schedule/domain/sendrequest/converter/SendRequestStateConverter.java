package com.ums.schedule.domain.sendrequest.converter;

import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class SendRequestStateConverter extends StatusStateConverter {
    public SendRequestStateConverter() {
        super(SendRequestStatusEnum.class);
    }
}
