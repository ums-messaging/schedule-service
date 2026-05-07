package com.ums.schedule.domain.target.converter;

import com.ums.schedule.code.EnumMapperConverter;
import com.ums.schedule.code.EnumMapperType;
import com.ums.schedule.code.send.SendTargetStatusEnum;
import jakarta.persistence.Converter;

@Converter
public class SendTargetStatusConverter extends EnumMapperConverter {
    public SendTargetStatusConverter() {
        super(SendTargetStatusEnum.class);
    }
}
