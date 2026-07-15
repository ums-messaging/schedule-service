package com.ums.schedule.common.code.request;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendRequestEnumMapper implements EnumMapper {
    SEND_REQUEST_STATUS(SendRequestStatus.class),
    GROUP_EVENT_TYPE(SendGroupEventTypeEnum.class),
    SEND_REQUEST_EVENT(SendRequestEvent.class)
    ;

    Class<? extends EnumMapperType> code;

    SendRequestEnumMapper(Class<? extends EnumMapperType> code) {
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
