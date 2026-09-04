package com.ums.schedule.common.code.common;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum DateTimeFormat implements EnumMapperType {
    YYYY_MM_DD("YYYY-MM-dd", ""),
    YYYYMMDD("YYYYMMdd", ""),
    YYYYMMDD_HHMMSS("YYYYMMdd HHmmss", ""),
    YYYY_MM_DD_HH_MM_SS("YYYY-MM-dd HH:mm:ss", "")
;
    String value;
    String description;

    DateTimeFormat(String value, String description) {
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
