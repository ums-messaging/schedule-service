package com.ums.schedule.common.code;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum DateTimeFormatEnum implements EnumMapperType {
    YYYY_MM_DD("YYYY-MM-dd", ""),
    YYYYMMDD("YYYYMMdd", ""),
    YYYYMMDD_HHMMSS("YYYYMMdd HHmmss", ""),
    YYYY_MM_DD_HH_MM_SS("YYYY-MM-dd HH:mm:ss", "")
;
    String value;
    String description;

    DateTimeFormatEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
