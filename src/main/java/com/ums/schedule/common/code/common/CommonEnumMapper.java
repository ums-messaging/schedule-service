package com.ums.schedule.common.code.common;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum CommonEnumMapper implements EnumMapperType {
    CHANNEL_TYPE(ChannelType.class),
    STORAGE_TYPE(StorageType.class),
    CHARSET(CharsetType.class),
    DATETIME_FORMAT(DateTimeFormat.class),
    FILE_CONTENT_TYPE(FileContentType.class)
    ;

    Class<? extends EnumMapperType> code;

    CommonEnumMapper(Class<? extends EnumMapperType> code) {
        this.code = code;
    }

    @Override
    public String code() {
        return null;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
