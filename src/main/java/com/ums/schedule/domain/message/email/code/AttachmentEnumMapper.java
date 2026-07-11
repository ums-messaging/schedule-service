package com.ums.schedule.domain.message.email.code;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.common.code.StorageTypeEnum;

public enum AttachmentEnumMapper implements EnumMapper {
    CONVERT_TYPE(ConvertTypeEnum.class),

    STORAGE_TYPE(StorageTypeEnum.class)
    ;
    Class<? extends EnumMapperType> code;

    AttachmentEnumMapper(Class<? extends EnumMapperType> code) {
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
