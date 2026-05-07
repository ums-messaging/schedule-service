package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum ChannelTypeEnum implements EnumMapperType {

    EMAIL("E", "이메일")

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
