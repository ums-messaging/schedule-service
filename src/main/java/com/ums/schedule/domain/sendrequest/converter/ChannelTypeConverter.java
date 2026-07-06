package com.ums.schedule.domain.sendrequest.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import jakarta.persistence.Converter;

@Converter
public class ChannelTypeConverter extends EnumMapperConverter {
    public ChannelTypeConverter() {
        super(ChannelTypeEnum.class);
    }
}
