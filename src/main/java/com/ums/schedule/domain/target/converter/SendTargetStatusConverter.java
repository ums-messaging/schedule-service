package com.ums.schedule.domain.target.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.converter.StatusStateConverter;
import jakarta.persistence.Converter;

@Converter
public class SendTargetStatusConverter extends StatusStateConverter {
    public SendTargetStatusConverter() {
        super(SendTargetStatus.class);
    }
}
