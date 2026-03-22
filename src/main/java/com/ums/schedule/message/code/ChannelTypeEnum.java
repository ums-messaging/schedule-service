package com.ums.schedule.message.code;

import com.ums.schedule.common.code.EnumMapperType;

public enum ChannelTypeEnum implements EnumMapperType {

    EMAIL("EMAIL", "이메일")

    ;

    String value;
    String description;

    ChannelTypeEnum(String value, String description) {
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
