package com.ums.schedule.message.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum EncodingTypeEnum implements EnumMapperType {
    BASE64("BASE64", "");

    String value;
    String description;

    EncodingTypeEnum(String value, String description) {
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
