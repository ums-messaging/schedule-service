package com.ums.schedule.message.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum CharsetEnum implements EnumMapperType  {
    EUC("EUC-KR", ""),
    UTF8("UTF-8", "");

    String value;
    String description;

    CharsetEnum(String value, String description) {
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
