package com.ums.schedule.common.code.common;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum CharsetType implements EnumMapperType {
    EUC("EUC-KR", ""),
    UTF8("UTF-8", "");

    String value;
    String description;

    CharsetType(String value, String description) {
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
