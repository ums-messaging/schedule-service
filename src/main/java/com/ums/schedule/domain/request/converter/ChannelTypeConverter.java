package com.ums.schedule.domain.request.converter;

import com.ums.schedule.common.code.mapper.EnumMapperConverter;
import com.ums.schedule.common.code.common.ChannelType;
import jakarta.persistence.Converter;

@Converter
public class ChannelTypeConverter extends EnumMapperConverter {
    public ChannelTypeConverter() {
        super(ChannelType.class);
    }
}
