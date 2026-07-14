package com.ums.schedule.common.code.message;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendMessageEnumMapper implements EnumMapper {
    MESSAGE_TYPE(MessageType.class);

    Class<? extends EnumMapperType> code;

    SendMessageEnumMapper(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }
}
