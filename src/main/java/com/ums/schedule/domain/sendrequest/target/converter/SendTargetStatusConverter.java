package com.ums.schedule.domain.sendrequest.target.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.target.SendTargetStatusEnum;
import jakarta.persistence.Converter;

@Converter
public class SendTargetStatusConverter extends EnumMapperConverter {
    public SendTargetStatusConverter() {
        super(SendTargetStatusEnum.class);
    }
}
