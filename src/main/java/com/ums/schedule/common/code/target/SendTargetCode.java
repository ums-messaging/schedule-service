package com.ums.schedule.common.code.target;

import com.ums.schedule.common.code.mapper.EnumMapper;
import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendTargetCode implements EnumMapper {
    TARGET_STATUS(SendTargetStatus.class),
    TARGET_COLUMN(SendTargetColumn.class),
    TARGET_ERROR_CODE(SendTargetResultCode.class)
    ;

    Class<? extends EnumMapperType> clz;

    SendTargetCode(Class<? extends EnumMapperType> clz) {
        this.clz = clz;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public Class<? extends EnumMapperType> code() {
        return this.clz;
    }
}
