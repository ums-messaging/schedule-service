package com.ums.schedule.common.code.common;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum CommonEnumMapper implements EnumMapper {
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
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.code;
    }


}
