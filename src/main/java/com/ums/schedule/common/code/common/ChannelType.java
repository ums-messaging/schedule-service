package com.ums.schedule.common.code.common;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum ChannelType implements EnumMapperType {

    EMAIL("E", "이메일")

    ;

    String value;
    String description;

    ChannelType(String value, String description) {
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
