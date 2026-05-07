package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperConverter;
import com.ums.schedule.code.EnumMapperType;
import jakarta.persistence.Converter;

@Converter
public class ChanelTypeConverter extends EnumMapperConverter {
    public ChanelTypeConverter() {
        super(ChannelTypeEnum.class);
    }
}
